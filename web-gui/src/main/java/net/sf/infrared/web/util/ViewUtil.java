/*
 *
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
 * Original Author:  kaushal.kumar (Tavant Technologies)
 * Contributor(s):   prashant.nair;
 *
 */
package net.sf.infrared.web.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.LayerTime;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.base.util.TreeNode;

import org.apache.log4j.Logger;

/**
 * Utility methods used by InfraRED web GUI.
 * 
 */
public class ViewUtil {
    private static BeanComparator nameComparator = new BeanComparator("getName", true);

    private static BeanComparator avgTimeComparator = new BeanComparator("getAverageInclusiveTime", true);

    private static BeanComparator totalTimeComparator = new BeanComparator("getTotalInclusiveTime", true);

    private static BeanComparator maxTimeComparator = new BeanComparator("getMaxInclusiveTime", true);

    private static BeanComparator minTimeComparator = new BeanComparator("getMinInclusiveTime", true);

    private static BeanComparator lastExcTimeComparator = 
                                        new BeanComparator("getInclusiveLastExecutionTime", true);

    private static BeanComparator countComparator = new BeanComparator("getExecutionCount", true);

    private static BeanComparator firstExcTimeComparator = 
                                        new BeanComparator("getInclusiveFirstExecutionTime", true);

    private static BeanComparator adjAvgTimeComparator = 
                                        new BeanComparator("getAdjAverageInclusiveTime",true);

    private static BeanComparator avgTimeExclusiveComparator = 
                                        new BeanComparator("getAverageExclusiveTime", true);

    private static BeanComparator totalTimeExclusiveComparator = 
                                        new BeanComparator("getTotalExclusiveTime", true);

    private static BeanComparator maxTimeExclusiveComparator = 
                                        new BeanComparator("getMaxExclusiveTime", true);

    private static BeanComparator minTimeExclusiveComparator = 
                                        new BeanComparator("getMinExclusiveTime", true);

    private static BeanComparator lastExcTimeExclusiveComparator = 
                                        new BeanComparator("getExclusiveLastExecutionTime", true);

    private static BeanComparator firstExcTimeExclusiveComparator = 
                                        new BeanComparator("getExclusiveFirstExecutionTime", true);

    private static BeanComparator adjAvgTimeExclusiveComparator = 
                                        new BeanComparator("getAdjAverageExclusiveTime", true);

    private static Logger logger = LoggingFactory.getLogger(ViewUtil.class.getName());

    /**
     * Gets the String format of the given double with two places after decimal
     * point
     * 
     * @param time
     * @return
     */
    public static String getFormattedTime(double time) {
        return getFormattedTime(time, 2);
    }

    /**
     * Gets the String format of the given double with the given number of
     * integers after the decimal point.
     * 
     * @param time
     * @param precision
     *            the number of places after the decimal point.
     * @return
     */

    public static String getFormattedTime(double time, int precision) {
        String formattedTime = (new Double(time)).toString();
        return getFormattedTime(formattedTime, precision);
    }

    public static String getFormattedTime(String time, int precision) {
        String formattedTime = (new Double(time)).toString();
        if (formattedTime != null && formattedTime.indexOf(".") != -1) {
            if (formattedTime.length() > (formattedTime.indexOf(".") + precision + 1)) {
                formattedTime = 
                    formattedTime.substring(0, formattedTime.indexOf(".") + precision+ 1);
            } 
            else {
                int numberOfZeorsToPad = 
                    (formattedTime.indexOf(".") + precision + 1)- formattedTime.length();
                for (int i = 0; i < numberOfZeorsToPad; i++) {
                    formattedTime += "0";
                }
            }
        }
        return formattedTime;
    }

    /**
     * Sorts an array of TimeVo object in-place. The sort criteria is determined
     * by the sortBy and sortDir request parameters.
     * 
     * @param arr
     * @param request
     */
    public static void sort(AggregateExecutionTime[] arr, HttpServletRequest request) {
        String sortBy = request.getParameter("sortBy");
        String sortDir = request.getParameter("sortDir");
        String exclusiveInclusiveMode = 
            (String) request.getSession().getAttribute("exclusiveInclusiveMode");

        if (sortBy == null) {
            sortBy = "adjAvg";
            if (exclusiveInclusiveMode != null && exclusiveInclusiveMode.equals("exclusive")){
                sortBy = "adjAvgExclusive";
            }                
        }
        

        if (sortDir == null) {
            sortDir = "desc";
        }
        boolean ascending = true;
        if (sortDir.equals("desc")) {
            ascending = false;
        }
        sort(sortBy, ascending, arr);
    }


    public static void sort(String sortBy, boolean ascending, AggregateExecutionTime[] arr) {
        BeanComparator comparator = getComparator(sortBy);
        comparator.setAscending(ascending);
        Arrays.sort(arr, comparator);
    }

    public static void sort(LayerTime[] layerTimes, String fieldName, boolean ascending) {
        BeanComparator comparator = null;
        if ("time".equals(fieldName)) {
            comparator = new BeanComparator("getTime", ascending);
        } 
        else if ("name".equals(fieldName)) {
            comparator = new BeanComparator("getLayer", ascending);
        } 
        else {
            comparator = new BeanComparator("getTime", ascending);
        }
        Arrays.sort(layerTimes, comparator);
    }

    public static AggregateExecutionTime[] getSummaryForALayer(PerformanceDataSnapshot perfData,
                                                                String layer) {        
        return perfData.getStats().getExecutionsInHierarchicalLayer(layer);
    }

    public static AggregateExecutionTime[] getSummaryForAbsoluteLayer(PerformanceDataSnapshot perfData,
                                                                String layer) {        
        return perfData.getStats().getExecutionsInAbsoluteLayer(layer);
    }

    public static AggregateExecutionTime[] getSummaryForALayer(HttpServletRequest request,
                                                                HttpSession session, 
                                                                String apiType, String layerType) {
        AggregateExecutionTime[] vos = null;
        PerformanceDataSnapshot perfData = 
                            (PerformanceDataSnapshot) session.getAttribute("perfData");
        
        if(layerType.equals("abs")){
            vos = getSummaryForAbsoluteLayer(perfData, apiType);
        }
        else if(layerType.equals("hier")){
            vos = getSummaryForALayer(perfData, apiType);
        }
        sort(vos, request);
        return vos;
    }

    public static AggregateExecutionTime [] getJDBCSummary(TreeNode node) {    	
    	Map jdbcMap = new HashMap();
    	getJDBCSummary(node, jdbcMap);
    	
    	AggregateExecutionTime [] jdbcExecTime = new AggregateExecutionTime[jdbcMap.size()];    	    	
    	jdbcMap.values().toArray(jdbcExecTime);
    	
    	return jdbcExecTime;
    }
    
    private static void getJDBCSummary(TreeNode node, Map jdbcMap){
        AggregateExecutionTime originalExecTime = (AggregateExecutionTime) node.getValue();
        
        // Needs to make modifications on a copy of this execTime, since the reference to  
        // the same is stored in the session.        
        AggregateExecutionTime execTime = (AggregateExecutionTime) originalExecTime.clone();

        if(execTime.getContext().getLayer().endsWith("JDBC")){
        	AggregateExecutionTime jdbcTime = (AggregateExecutionTime)
        												jdbcMap.get(execTime.getContext());
        	if(jdbcTime == null){
        		jdbcMap.put(execTime.getContext(), execTime);
        	}
        	else{
        		jdbcTime.merge(execTime);
        	}        	
        }
        for (Iterator itr = node.getChildren().iterator(); itr.hasNext();) {
            getJDBCSummary((TreeNode) itr.next(), jdbcMap);
        }    	
        
    	return;
    }
    
    public static SqlStatistics [] getSqlStatistics(TreeNode node){
    	Map sqlMap = new HashMap();
    	getSqlStatistics(node, sqlMap);
    	SqlStatistics [] sqlExecTime = new SqlStatistics[sqlMap.size()];
    	
    	sqlMap.values().toArray(sqlExecTime);
    	return sqlExecTime;
    }
    
    public static void getSqlStatistics(TreeNode node, Map sqlMap){
    	AggregateExecutionTime originalSqlExec = (AggregateExecutionTime) node.getValue();
    	
        // Needs to make modifications on a copy of this sqlExec, since the reference to  
        // the same is stored in the session.
    	AggregateExecutionTime sqlExec = (AggregateExecutionTime) originalSqlExec.clone();
    	    	
    	if(sqlExec.getContext().getLayer().equals("SQL")){
    		ExecutionContext sqlContext = sqlExec.getContext();
    		String sql = sqlContext.getName();
    		
    		SqlStatistics sqlStats = (SqlStatistics)sqlMap.get(sql);
    		if(sqlStats == null){
    			sqlStats = new SqlStatistics();
    			sqlMap.put(sql, sqlStats);
    		}
    		if(sqlContext instanceof SqlPrepareContext){
    			sqlStats.mergePrepareTime(sqlExec);
    		}
    		if(sqlContext instanceof SqlExecuteContext){
    			sqlStats.mergeExecuteTime(sqlExec);
    		}    		
    	}
        for (Iterator itr = node.getChildren().iterator(); itr.hasNext();) {
        	getSqlStatistics((TreeNode) itr.next(), sqlMap);
        }    	
    	
		return;
    }

    private static BeanComparator getComparator(String sortBy) {
        if (sortBy.equals("name") || sortBy.equals("nameExclusive")) {
            return nameComparator;
        }
        if (sortBy.equals("time")) {
            return avgTimeComparator;
        }
        if (sortBy.equals("count") || sortBy.equals("countExclusive")) {
            return countComparator;
        }
        if (sortBy.equals("totalTime")) {
            return totalTimeComparator;
        }
        if (sortBy.equals("maxTime")) {
            return maxTimeComparator;
        }
        if (sortBy.equals("minTime")) {
            return minTimeComparator;
        }
        if (sortBy.equals("lastExecTime")) {
            return lastExcTimeComparator;
        }
        if (sortBy.equals("first")) {
            return firstExcTimeComparator;
        }

        if (sortBy.equals("adjAvg")) {
            return adjAvgTimeComparator;
        }

        if (sortBy.equals("timeExclusive")) {
            return avgTimeExclusiveComparator;
        }
        if (sortBy.equals("totalTimeExclusive")) {
            return totalTimeExclusiveComparator;
        }
        if (sortBy.equals("maxTimeExclusive")) {
            return maxTimeExclusiveComparator;
        }
        if (sortBy.equals("minTimeExclusive")) {
            return minTimeExclusiveComparator;
        }
        if (sortBy.equals("lastExecTimeExclusive")) {
            return lastExcTimeExclusiveComparator;
        }
        if (sortBy.equals("firstExclusive")) {
            return firstExcTimeExclusiveComparator;
        }
        if (sortBy.equals("adjAvgExclusive")) {
            return adjAvgTimeExclusiveComparator;
        }
        return nameComparator;
    }

     public static SqlStatistics[] getTopNQueriesByExecutionTime(SqlStatistics[] sqlStatistics, 
                                                                 int n) {
        Arrays.sort(sqlStatistics, new BeanComparator("getAvgExecuteTime", false));
        if (n != 0) {
            return getTopN(sqlStatistics, n);
        }
        return sqlStatistics;
    }
    
     public static SqlStatistics[] getTopNQueriesByCount(SqlStatistics[] sqlStatistics, int n) {
        Arrays.sort(sqlStatistics, new BeanComparator("getNoOfExecutes", false));
        if (n != 0) {
            return getTopN(sqlStatistics, n);
        }
        return sqlStatistics;
    }

     private static SqlStatistics[] getTopN(SqlStatistics[] sqlStatistics, int n) {
        SqlStatistics[] result = sqlStatistics;
        if (sqlStatistics.length > n) {
            SqlStatistics[] temp = new SqlStatistics[n];
            System.arraycopy(sqlStatistics, 0, temp, 0, n);
            result = temp;
        }
        return result;
    }

    public static String extractMethodName(String name) {
        int methodNameStart = name.lastIndexOf(".");
        int classNameStart = name.lastIndexOf(".", methodNameStart - 1);
        if (classNameStart == -1) {
            return name;
        } else {
            return name.substring(classNameStart + 1);
        }
    }

    public static String getSortByHrefInLayer(String sortBy, String sortDir, String currentSortBy,
                                                String apiType, String exclusiveInclusiveMode) {
        String correctedSortBy = sortBy;
        if (exclusiveInclusiveMode.equals("exclusive")){
            correctedSortBy = correctedSortBy.concat("Exclusive");
        }
            
        if (sortDir == null && (sortBy.indexOf("adjAvg") != -1)){
            return "perfData_layerSummaryAction.do?type=" + apiType + "&sortBy=" 
                    + correctedSortBy + "&sortDir=asc";            
        }
        
        if (correctedSortBy.equals(currentSortBy)) {
            if (sortDir.equals("desc")){
                return "perfData_layerSummaryAction.do?type=" + apiType + "&sortBy="
                        + correctedSortBy + "&sortDir=asc";                
            }
            else{
                return "perfData_layerSummaryAction.do?type=" + apiType + "&sortBy="
                        + correctedSortBy + "&sortDir=desc";                
            }
        } 
        else{
            return "perfData_layerSummaryAction.do?type=" + apiType + "&sortBy=" 
                    + correctedSortBy + "&sortDir=desc";            
        }
    }

    public static String getSortByHrefInSummary(String sortBy, String sortDir, 
                                                String currentSortBy) {
        if (sortDir == null && sortBy.equals("time")){
            return "perfData_summaryAction.do?sortBy=" + sortBy + "&sortDir=asc";
        }
            
        if (sortBy.equals(currentSortBy)) {
            if (sortDir.equals("desc")){
                return "perfData_summaryAction.do?sortBy=" + sortBy + "&sortDir=asc";
            }                
            else{
                return "perfData_summaryAction.do?sortBy=" + sortBy + "&sortDir=desc";
            }
                
        } 
        else
            return "perfData_summaryAction.do?sortBy=" + sortBy + "&sortDir=desc";
    }

    public static String getSortByHrefInAbsSummary(String sortBy, String sortDir, 
                                                                        String currentSortBy) {
        if (sortDir == null && sortBy.equals("time")) {
            return "perfData_summaryAction.do?sortByAbs=" + sortBy + "&sortDirAbs=asc";
        }

        if (sortBy.equals(currentSortBy)) {
            if (sortDir.equals("desc")) {
                return "perfData_summaryAction.do?sortByAbs=" + sortBy + "&sortDirAbs=asc";
            }
            else {
                return "perfData_summaryAction.do?sortByAbs=" + sortBy + "&sortDirAbs=desc";
            }

        }
        else
            return "perfData_summaryAction.do?sortByAbs=" + sortBy + "&sortDirAbs=desc";
    }

    public static String getSortByIconInLayer(String sortBy, String sortDir, String currentSortBy,
                                                String exclusiveInclusiveMode) {
        String correctedSortBy = sortBy;
        if (exclusiveInclusiveMode.equals("exclusive")){
            correctedSortBy = correctedSortBy.concat("Exclusive");
        }
            
        if (sortDir == null && (sortBy.indexOf("adjAvg") != -1)){
            return "common/graphics/sort_dn.gif";
        }
            
        if (correctedSortBy.equals(currentSortBy)) {
            if (sortDir.equals("asc")){
                return "common/graphics/sort_up.gif";
            }                
            else{
                return "common/graphics/sort_dn.gif";
            }                
        } 
        else{
            return "common/graphics/notselected.gif";
        }
            
    }

    public static String getSortByIconInSummary(String sortBy, String sortDir, 
                                                String currentSortBy) {
        if (sortDir == null && sortBy.equals("time")){
            return "common/graphics/sort_dn.gif";
        }
            
        if (sortBy.equals(currentSortBy)) {
            if (sortDir.equals("asc")){
                return "common/graphics/sort_up.gif";
            }                
            else{
                return "common/graphics/sort_dn.gif";
            }               
        } 
        else{
            return "common/graphics/notselected.gif";
        }
            
    }
}
