/*
 * Copyright 2005 Tavant Technologies and Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * Original Author:  subin.p (Tavant Technologies)
 * Contributor(s):   binil.thomas;
 *
 */
package net.sf.infrared.collector.impl.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.AggregateOperationTree;
import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.LayerTime;
import net.sf.infrared.base.model.StatisticsSnapshot;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.collector.ApplicationStatisticsDao;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ApplicationStatisticsDaoImpl
        extends JdbcDaoSupport implements ApplicationStatisticsDao {

    private static final String SQL_INSERT_LAYER_TIME =
            "insert into LAYER_TIMES (APP_NAME, HOST_NAME, LAYER, DURATION, INSERT_TIME) " +
            "values(?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_AGGREGATE_EXECUTION_TIME =
            "insert into EXECUTION_TIMES(APP_NAME, HOST_NAME, LAYER, CLASS_NAME, NAME, EXEC_COUNT, "
            + "TOT_INCLUSIVE_TIME, MAX_INCLUSIVE_TIME, MIN_INCLUSIVE_TIME, TOT_EXCLUSIVE_TIME, "
            + "MAX_EXCLUSIVE_TIME, MIN_EXCLUSIVE_TIME, TIME_OF_FST_EXEC, TIME_OF_LST_EXEC, "
            + "INCLUSIVE_FST_EXEC_TIME, INCLUSIVE_LST_EXEC_TIME, EXCLUSIVE_FST_EXEC_TIME, "
            + "EXCLUSIVE_LST_EXEC_TIME, HIERARCHICAL_LAYER, INSERT_TIME) "
            + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_TREE =
            "insert into AGG_OPERATION_TREE(APP_NAME, HOST_NAME, TREE, INSERT_TIME) "
            + "values(?, ?, ?, ?)";

    private static final String SQL_FETCH_LAYER_TIME_BASE =
            "select * from LAYER_TIMES " +
            "WHERE ( INSERT_TIME > ? AND INSERT_TIME < ?) AND (APP_NAME IN (";

    private static final String SQL_FETCH_AGGREGATE_EXECUTION_TIME =
            "select * from EXECUTION_TIMES " +
            "WHERE ( INSERT_TIME > ? AND INSERT_TIME < ?) AND (APP_NAME IN (";

    private static final String SQL_FETCH_TREE =
            "select * from AGG_OPERATION_TREE " +
            "where ( INSERT_TIME > ? AND INSERT_TIME < ?) AND (APP_NAME IN (";

    private static final int MAX_LENGTH_OF_NAME = 1000;

    private static final Logger log = LoggingFactory.getLogger(ApplicationStatisticsDaoImpl.class);

    private int argumentCount;

    private DataSource dataSource;

    public ApplicationStatisticsDaoImpl(DataSource ds) {
        setDataSource(ds);
        this.dataSource = ds;
    }

    public ApplicationStatisticsDaoImpl() {
    }

    public void saveStatistics(ApplicationStatistics[] stats) {
        for (int i = 0; i < stats.length; i++) {
            saveStatistics(stats[i]);
        }
    }

    public void saveStatistics(ApplicationStatistics stats) {
        if (stats == null) {
            throw new IllegalArgumentException("The ApplicationStatistics to be saved is null");
        }
        if (log.isDebugEnabled()) {
            log.debug("Going to save " + stats + " to DB");
        }

        synchronized (stats) {
            saveLayerTimes(stats);
            saveExecutionTimes(stats);
            saveAggregateOperationTree(stats);
        }

        if (log.isDebugEnabled()) {
            log.debug("Saved statistics " + stats + " to DB");
        }
    }

    public StatisticsSnapshot fetchStatistics(Collection appNames,
            Collection instanceIds, Date fromDate, Date toDate) {
        if ( (appNames == null) || (appNames.isEmpty())
            || (instanceIds == null) || (instanceIds.isEmpty()) ) {
            return null;
        }
        Collection results = null;

        Object[] queryAndArgs =
                getQueryAndArgsForFetchingExecutionTimes(appNames, instanceIds, fromDate, toDate);
        results = getJdbcTemplate().queryForList((String) queryAndArgs[0], (Object[]) queryAndArgs[1]);
        Map executionTimes = getLayerToExecutionTimeMap(results);

        queryAndArgs = getQueryAndArgsForFetchingLayerTimes(appNames, instanceIds, fromDate, toDate);
        results = getJdbcTemplate().queryForList((String) queryAndArgs[0], (Object[]) queryAndArgs[1]);
        Map layerTimes = getLayerTimesMap(results);

        AggregateOperationTree aggOperTree =
                fetchAggregateOperationTree(appNames, instanceIds, fromDate, toDate);

        return StatisticsSnapshot.createSnapshot(appNames, instanceIds, layerTimes,
                executionTimes, aggOperTree, fromDate.getTime(), toDate.getTime());
    }

    //Need to check if this is ok. Need for testing purpose.
    public DataSource getDaoDataSource() {
    	return this.dataSource;
    }


    // @TODO probably it is inefficient to figure out the query where clause and arg array thrice
    // one each for fetching execution times, layer times and tree, but this way the code looks
    // clearer
    Object[] getQueryAndArgsForFetchingExecutionTimes(Collection appNames,
            Collection instanceIds, Date fromDate, Date toDate) {
        Object[] stringAndArray = getWhereStringAndArgArray(appNames, instanceIds, fromDate, toDate);
        String queryWhereClause = (String) stringAndArray[0];
        Object[] argArray = (Object[]) stringAndArray[1];

        return new Object[] { SQL_FETCH_AGGREGATE_EXECUTION_TIME + queryWhereClause, argArray };
    }

    Object[] getQueryAndArgsForFetchingLayerTimes(Collection appNames,
            Collection instanceIds, Date fromDate, Date toDate) {
        Object[] stringAndArray = getWhereStringAndArgArray(appNames, instanceIds, fromDate, toDate);
        String queryWhereClause = (String) stringAndArray[0];
        Object[] argArray = (Object[]) stringAndArray[1];

        return new Object[] { SQL_FETCH_LAYER_TIME_BASE + queryWhereClause, argArray };
    }

    Object[] getWhereStringAndArgArray(Collection appNames,
            Collection instanceIds, Date fromDate, Date toDate) {
        argumentCount = 1;
        Object[] argArray = getArgumentArray(fromDate, toDate, appNames.size() + instanceIds.size());
        StringBuffer query = new StringBuffer();

        Object[] results = processQueryAndArgArrayForCollection(argArray, query, appNames);
        query = (StringBuffer) results[0];
        argArray = (Object[]) results[1];
        query.append(") AND HOST_NAME IN (");

        results = processQueryAndArgArrayForCollection(argArray, query, instanceIds);
        query = (StringBuffer) results[0];
        argArray = (Object[]) results[1];
        query.append(") )");

        results[0] = query.toString();
        results[1] = argArray;

        return results;
    }

    Object[] processQueryAndArgArrayForCollection(Object[] argArray, StringBuffer query,
            Collection appNames) {
        int length = 0;
        Object[] returnValues = new Object[2];

        for (Iterator iter = appNames.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            argArray[++argumentCount] = element;
            query.append("?, ");
        }
        length = query.length();
        query.delete(length - 2, length - 1);
        returnValues[0] = query;
        returnValues[1] = argArray;

        return returnValues;
    }

    Map getLayerTimesMap(Collection results) {
        String layerName = null;
        LayerTime mapLayerTime = null;
        long duration = 0;
        Map layerTimesMap = new HashMap();

        for (Iterator iter = results.iterator(); iter.hasNext();) {
            Map element = (Map) iter.next();
            layerName = (String) element.get("LAYER");

            if (layerTimesMap.get(layerName) == null)
                layerTimesMap.put(layerName, constructLayerTime(element));
            else {
                mapLayerTime = (LayerTime) layerTimesMap.get(layerName);
                duration = ((BigDecimal) element.get("DURATION")).longValue();
                mapLayerTime.addToTime(duration);
            }
        }
        return layerTimesMap;
    }

    Map getLayerToExecutionTimeMap(Collection results) {
        Map layerToAetsMap = new HashMap();
        Class[] argumentsClass = new Class[] { String.class, String.class };
        Object[] argumentObjects = new Object[2];

        for (Iterator iter = results.iterator(); iter.hasNext();) {
            Map element = (Map) iter.next();
            String name = (String) element.get("NAME");
            String ctxLayer = (String) element.get("LAYER");
            String className = (String) element.get("CLASS_NAME");

            argumentObjects[0] = name;
            argumentObjects[1] = ctxLayer;
            ExecutionContext newContext =
                    getNewExecContextFromName(className, argumentsClass, argumentObjects);
            // Meaningless to save times without a valid context.
            if (newContext == null) {
                log.debug("Ignoring one row because we could not create the ExecutionContext for it");
                continue;
            }
            String layerName = (String) element.get("HIERARCHICAL_LAYER");
            //String layerName = (String) element.get("LAYER");
            // No point proceeding to creating the AET for this
            if (layerName == null) {
                log.debug("Ignoring one row because there is no hierarchical layer info for it");
                continue;
            }
            AggregateExecutionTime aet = getAggregateExecutionTime(element, newContext);
            List aetsInTheLayer = (List) layerToAetsMap.get(layerName);
            if (aetsInTheLayer == null) {
                aetsInTheLayer = new ArrayList();
                layerToAetsMap.put(layerName, aetsInTheLayer);
            }
            aetsInTheLayer.add(aet);
        }

        return layerToAetsMap;
    }

    ExecutionContext getNewExecContextFromName(String className,
            Class[] argClasses, Object[] args) {

        ExecutionContext ctx = null;

        try {
            Class ctxClass = Class.forName(className);
            ctx = (ExecutionContext) ctxClass.getConstructor(argClasses).newInstance(args);
        } catch (NoSuchMethodException ex) {
            logger.error("The two arguments constructor for " + className + " doesn't exist", ex);
            return null;
        } catch (InstantiationException ex) {
            logger.error("The class" + className + "cannot be instantiated", ex);
            return null;
        } catch (InvocationTargetException ex) {
            logger.error("InvocationTargetException in class" + className, ex);
            return null;
        } catch (IllegalAccessException ex) {
            logger.error("IllegalAccessException on constructor of class" + className, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            logger.error("ClassNotFoundException on class" + className, ex);
            return null;
        }

        return ctx;
    }

    AggregateExecutionTime getAggregateExecutionTime(Map element, ExecutionContext context) {
        long longVal = 0;
        AggregateExecutionTime aggExecTime = new AggregateExecutionTime(context);

        longVal = ((BigDecimal) element.get("EXEC_COUNT")).longValue();
        aggExecTime.setExecutionCount((int) longVal);

        longVal = ((BigDecimal) element.get("EXCLUSIVE_FST_EXEC_TIME")).longValue();
        aggExecTime.setExclusiveFirstExecutionTime(longVal);

        longVal = ((BigDecimal) element.get("EXCLUSIVE_LST_EXEC_TIME")).longValue();
        aggExecTime.setExclusiveLastExecutionTime(longVal);

        longVal = ((BigDecimal) element.get("INCLUSIVE_FST_EXEC_TIME")).longValue();
        aggExecTime.setInclusiveFirstExecutionTime(longVal);

        longVal = ((BigDecimal) element.get("INCLUSIVE_LST_EXEC_TIME")).longValue();
        aggExecTime.setInclusiveLastExecutionTime(longVal);

        longVal = ((BigDecimal) element.get("MAX_EXCLUSIVE_TIME")).longValue();
        aggExecTime.setMaxExclusiveTime(longVal);

        longVal = ((BigDecimal) element.get("MAX_INCLUSIVE_TIME")).longValue();
        aggExecTime.setMaxInclusiveTime(longVal);

        longVal = ((BigDecimal) element.get("MIN_EXCLUSIVE_TIME")).longValue();
        aggExecTime.setMinExclusiveTime(longVal);

        longVal = ((BigDecimal) element.get("MIN_INCLUSIVE_TIME")).longValue();
        aggExecTime.setMinInclusiveTime(longVal);

        longVal = ((BigDecimal) element.get("TIME_OF_FST_EXEC")).longValue();
        aggExecTime.setTimeOfFirstExecution(longVal);

        longVal = ((BigDecimal) element.get("TIME_OF_LST_EXEC")).longValue();
        aggExecTime.setTimeOfLastExecution(longVal);

        longVal = ((BigDecimal) element.get("TOT_EXCLUSIVE_TIME")).longValue();
        aggExecTime.setTotalExclusiveTime(longVal);

        longVal = ((BigDecimal) element.get("TOT_INCLUSIVE_TIME")).longValue();
        aggExecTime.setTotalInclusiveTime(longVal);

        String layerName = ((String) element.get("HIERARCHICAL_LAYER"));
        aggExecTime.setLayerName(layerName);

        return aggExecTime;
    }

    LayerTime constructLayerTime(Map elements) {
        long duration = ((BigDecimal) elements.get("DURATION")).longValue();

        LayerTime layerTime = new LayerTime((String) elements.get("LAYER"));
        layerTime.setTime(duration);

        return layerTime;
    }

    void saveExecutionTimes(final ApplicationStatistics stats) {
        final String appName = stats.getApplicationName();
        final String instanceId = stats.getInstanceId();

        final String[] layers = stats.getLayers();
        for (int i = 0; i <layers.length; i++) {
            final AggregateExecutionTime[] aets = stats.getExecutionsInLayer(layers[i]);
            final String layerName = layers[i];
            getJdbcTemplate().batchUpdate(SQL_INSERT_AGGREGATE_EXECUTION_TIME,
                    new BatchPreparedStatementSetter() {
                public int getBatchSize() {
                    return aets.length;
                }

                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    String name = aets[j].getContext().getName();

                    ps.setString(1, appName);
                    ps.setString(2, instanceId);
                    ps.setString(3, aets[j].getContext().getLayer());
                    ps.setString(4, aets[j].getContext().getClass().getName());

                    // since sql query strigs can be very large and hence those shall be stored
                    // after trimming.
                    if(name!= null && name.length() > MAX_LENGTH_OF_NAME) {
                        name = name.substring(0, MAX_LENGTH_OF_NAME - 1);
                        log.debug("The name element : " + name + " of the " + aets[j].getContext() +
                                " context exceeded its maximum allotted length of " + MAX_LENGTH_OF_NAME +
                                " and shall be trimmed down to the permitted size before persisting to database.");
                    }
                    ps.setString(5, name);


                    ps.setLong(6, aets[j].getExecutionCount());
                    ps.setLong(7, aets[j].getTotalInclusiveTime());
                    ps.setLong(8, aets[j].getMaxInclusiveTime());
                    ps.setLong(9, aets[j].getMinInclusiveTime());
                    ps.setLong(10, aets[j].getTotalExclusiveTime());
                    ps.setLong(11, aets[j].getMaxExclusiveTime());
                    ps.setLong(12, aets[j].getMinExclusiveTime());
                    ps.setLong(13, aets[j].getTimeOfFirstExecution());
                    ps.setLong(14, aets[j].getTimeOfLastExecution());
                    ps.setLong(15, aets[j].getInclusiveFirstExecutionTime());
                    ps.setLong(16, aets[j].getInclusiveLastExecutionTime());
                    ps.setLong(17, aets[j].getExclusiveFirstExecutionTime());
                    ps.setLong(18, aets[j].getExclusiveLastExecutionTime());
                    ps.setString(19, aets[j].getLayerName());
                    ps.setTimestamp(20, new Timestamp(System.currentTimeMillis()));
                }
            });
            if(log.isDebugEnabled()) {
                log.debug("Scheduled batch update for saving " + aets.length +
                        " executions times in layer " + layerName + " to DB; stats=" + stats);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Saved all execution times of " + layers.length +
                    " layers in " + stats + " to DB");
        }
    }

    void saveLayerTimes(final ApplicationStatistics stats) {
        final String appName = stats.getApplicationName();
        final String instanceId = stats.getInstanceId();

        final String[] layers = stats.getLayers();

        getJdbcTemplate().batchUpdate(SQL_INSERT_LAYER_TIME, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return layers.length;
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, appName);
                ps.setString(2, instanceId);
                ps.setString(3, layers[i]);
                ps.setLong(4, stats.getTimeInLayer(layers[i]));
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            }
        });
        if (log.isDebugEnabled()) {
            log.debug("Saved " + layers.length + " layer times in " + stats + " to DB");
        }
    }

    void saveAggregateOperationTree(ApplicationStatistics stats) {
        String appName = stats.getApplicationName();
        String instanceId = stats.getInstanceId();
        AggregateOperationTree tree = stats.getTree();
        if (tree == null || tree.getAggregateTree() == null)
            return;

        insertTree(appName, instanceId, tree.getAggregateTree());
        if (log.isDebugEnabled()) {
            log.debug("Saved aggregate operation tree in " + stats + " to DB");
        }
    }


    AggregateOperationTree fetchAggregateOperationTree(Collection appNames,
            Collection instanceIds, Date fromDate, Date toDate) {
        Object[] stringAndArray = getWhereStringAndArgArray(appNames, instanceIds, fromDate, toDate);
        String whereClause = (String) stringAndArray[0];
        Object[] argArray = (Object[]) stringAndArray[1];

        // making use of a spring ResultSetExtractor implementation to get the merged tree.
        AggregateOperationTree aggOpTree = (AggregateOperationTree) getJdbcTemplate().query(
        		SQL_FETCH_TREE + whereClause, argArray, new ResultSetExtractor() {
        			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
        				AggregateOperationTree newTree = null;
        				AggregateOperationTree aggOpTree = new AggregateOperationTree();
        				while(rs.next()) {
        					try {
        						InputStream is = rs.getBinaryStream("TREE");
        						ObjectInputStream ois = new ObjectInputStream(is);
        						Tree tree = (Tree) ois.readObject();
        			            newTree = new AggregateOperationTree();
        			            newTree.setAggregateTree(tree);
        			            aggOpTree.merge(newTree);
        					}catch(IOException e) {
        						e.printStackTrace();
        					}catch(ClassNotFoundException ex) {
        						ex.printStackTrace();
        					}
        				}
        				return aggOpTree;
        			}
    			});

        return aggOpTree;
    }

    Object[] getArgumentArray(Date fromDate, Date toDate, int size) {
        Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
        Timestamp toTimeStamp = null;
        if (toDate != null) {
            toTimeStamp = new Timestamp(toDate.getTime());
        } else {
            toTimeStamp = new Timestamp(System.currentTimeMillis());
        }
        Object[] argArray = new Object[size + 2];
        argArray[0] = fromTimestamp;
        argArray[1] = toTimeStamp;
        return argArray;
    }

     private void insertTree (final String appName, final String hostName, final Tree tree) {
    	 byte[] byteArray = null;
    	 try{
	    	 ByteArrayOutputStream baos = serializeObject(tree);
			 byteArray = baos.toByteArray();
    	 }catch(IOException e) {
    		 log.error("IOException : Unable to serialize the Aggregate Operation Tree Object");
    	 }
    	 final ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
         getJdbcTemplate().update(SQL_INSERT_TREE, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
   		     ps.setString(1, appName);
		     ps.setString(2, hostName);
		     ps.setBinaryStream(3, bais, bais.available());
		     ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            }
         });
       }

	 private ByteArrayOutputStream serializeObject(Object obj) throws IOException {
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  if (null != obj) {
			  ObjectOutputStream out = new ObjectOutputStream(baos);
		      out.writeObject(obj);
		      out.flush();
	      }
		  return baos;
	  }

}
