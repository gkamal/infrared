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
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.collector.impl.persistence;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.aspects.jdbc.SqlContext;
import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.model.LayerTime;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.model.StatisticsSnapshot;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;
import net.sf.infrared.collector.ApplicationStatisticsDao;
import junit.framework.TestCase;

public class ApplicationStatisticsDaoImplTest extends TestCase {
			
	//TODO Need more Unit tests.       
    public void testSaveForNullStatistics() {
        SpringContext springContext = new SpringContext();       
        ApplicationStatisticsDao dao = springContext.getDao();
        try{
            dao.saveStatistics( (ApplicationStatistics) null);
            fail();
        } catch (IllegalArgumentException expected) {
        	assertTrue(true);
        } catch (Throwable t) {  
        	t.printStackTrace();
            fail();
        }       
    }
    
    public void testgetQueryAndArgsForFetchingExecutionTimes() {
        ApplicationStatisticsDaoImpl fixture = new ApplicationStatisticsDaoImpl();
        Set apps = new HashSet();
        apps.add("test-app");
        Set insts = new HashSet();
        // insts.add("test-inst");
        Date from = new Date(0);
        Object[] result = fixture.getQueryAndArgsForFetchingExecutionTimes(apps, insts, from, null);
        assertEquals(2, result.length);
        System.out.println("Query = " + result[0]);
        System.out.print("Agrs = ");
        Object[] args = (Object[]) result[1];
        for (int i = 0; i < args.length; i++) {
            System.out.print(args[i] + ", ");
        }
        System.out.println("");
    }
    
 
    public void testIfContextIsSerializable() throws Exception {
        Tree tree = getOperationTree();                
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        try {
            oos.writeObject(tree);
            oos.close();
        } catch (IOException e) {
            fail();
        }
    }

    
    public void testForSaveFetchPairOnFreshDB(){    	    			
        //  Instantiate the SpringContext with the testInfraDB.
        SpringContext sctx = new SpringContext(){
            public ApplicationStatisticsDao getDao() {                            	
            	return (ApplicationStatisticsDao) getContext().getBean("testInfraredDao");
            }        	        	
        };       
        
        // Assert fetch for null parameters.
        ApplicationStatisticsDao dao = sctx.getDao();          
        assertNull(dao.fetchStatistics(null, null, null, null));
        
        // Test save application statistics 
        ApplicationStatistics appStats = getApplicationStatisticsToSave();
        dao.saveStatistics(appStats);
                        
        // Wait for 2 secs before the fetch, since the todate for the db fetch is exclusive.
        waitABit();
        
        // Fetch the statisitics.
        ArrayList apps = new ArrayList();
        apps.add("iRes");            
        ArrayList hosts = new ArrayList();
        hosts.add("host1");                
        StatisticsSnapshot snapShotfromDb = dao.fetchStatistics(apps, hosts, new Date(103, 2, 12), new Date());
        checkSnapShotValidity(snapShotfromDb);
        sctx.shutdown();        
        clearDbFiles(dao);
    }
    
    
    private void clearDbFiles(ApplicationStatisticsDao dao) {
    	InProcessDataSource ipd = (InProcessDataSource)dao.getDaoDataSource();
    	File dbPropsFile = new File(ipd.getDbPath(), InProcessDataSource.DB_NAME + ".properties");
    	File dbScriptFile = new File(ipd.getDbPath(), InProcessDataSource.DB_NAME + ".script");
    	
    	dbPropsFile.delete();
    	dbScriptFile.delete();    	    	
    }
    
    private void waitABit() {
    	try {
    		Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Error waiting before fetching the saved statistics");
		}    	        	
    }
    
    private void checkSnapShotValidity(StatisticsSnapshot snapShotfromDb) {
    	assertEquals(snapShotfromDb.getApplicationNames().size(), 1);
    	assertEquals(snapShotfromDb.getAbsoluteLayers().length, 3);
    	
    	// Need more assertions. Check the snapShotfromDb.getExecutionsInAbsoluteLayer()
    	// code. Need to change the applicationStatistics that gets saved to reflect this.
    	
    	System.out.println(snapShotfromDb.getExecutionsInAbsoluteLayer("web").length);
    	System.out.println(snapShotfromDb.getExecutionsInHierarchicalLayer("web").length);
    	//assertEquals(snapShotfromDb.getExecutionsInAbsoluteLayer("web").length, 2);
    	
    	System.out.println("The tree is : ");

    	System.out.println(snapShotfromDb.getTree());
    	
    	//LayerTimeRepository layerRepo = snapShotfromDb.get
    }

    private ApplicationStatistics getApplicationStatisticsToSave() {
        String[] layers = new String[] {"web","business","SQL"};
        long[] times = new long[] {30, 23, 44};
        Map layerMap = createLayerTimesMap(layers, times);
        Map executionsMap = createExecMapWithLayerKey();
        Tree operTree = getOperationTree();
        
                        
        OperationStatistics opStats = new OperationStatistics("iRes", "host1");
        opStats.setLayerTimes(layerMap);
        opStats.setExecutionTimes(executionsMap);
        opStats.setOperationTree(operTree);
                
        System.out.println("*****appStats before save*****" + opStats);
        ApplicationStatistics appStats = new ApplicationStatistics("iRes", "host1");
        System.out.println("\n\n" + opStats.getOperationTree());
        appStats.merge(opStats);
                       
        return appStats;        
    } 
        
               
    private Map createExecMapWithLayerKey() {
    	Map returnMap = new HashMap();
    	List tempList = null;
    	ExecutionContext tempCtx = null;
    	
    	// Prepare ExecutionTimer list for web layer.
    	tempList = new ArrayList(); 
    	tempCtx = new ApiContext("com.web.package", "method1", "web");
    	tempList.add(getExecutionTimer(tempCtx, 300));
    	tempCtx = new ApiContext("com.web.package", "method2", "web");
    	tempList.add(getExecutionTimer(tempCtx, 500));
    	returnMap.put("web", tempList);

    	//Prepare ExecutionTimer list for business layer.
    	tempList = new ArrayList();
    	tempCtx = new ApiContext("com.business.package", "method3", "business");
    	tempList.add(getExecutionTimer(tempCtx, 400));
    	returnMap.put("business", tempList);

    	//Prepare ExecutionTimer list for SQL layer.
    	tempList = new ArrayList();
    	SqlContext parent = new SqlContext("select * from dual");    	
    	tempCtx = new SqlPrepareContext(parent);
    	tempList.add(getExecutionTimer(tempCtx, 300));
    	tempCtx = new SqlExecuteContext(parent);
    	tempList.add(getExecutionTimer(tempCtx, 200));
    	returnMap.put("SQL", tempList);

    	return returnMap;
    }
    
    
    private ExecutionTimer getExecutionTimer(ExecutionContext ctx, int millisecsToWait){
    	ExecutionTimer timer = new ExecutionTimer(ctx);
    	timer.start();
    	try {
    		Thread.sleep(millisecsToWait);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Error creating ExecutionTimer - testing save functionality");
		}    	    	
    	timer.stop();    	
    	return timer;
    }
    
    
    private Tree getOperationTree() {
    	
        //dummy        
        //    +---  api1
        //             +--- api2
        //                     +---sqlPrep1                      
        //                     +---sqlExec1
        
        //    +---  api2 
        //             +---sqlPrep1                      
        //             +---sqlExec1
    	
    	
    	ExecutionContext tempCtx = null;
    	
                           
        
    	tempCtx = new ApiContext("com.web.package", "method1", "web");
        TreeNode nodeApi1= TreeNode.createTreeNode(getExecutionTimer(tempCtx, 300));
    	tempCtx = new ApiContext("com.business.package", "method3", "business");    	                
        TreeNode nodeApi2= TreeNode.createTreeNode(getExecutionTimer(tempCtx, 200));
        
        SqlContext parent = new SqlContext("select * from dual");
        
    	tempCtx = new SqlPrepareContext(parent);
    	TreeNode nodeSqlPrep1= TreeNode.createTreeNode(getExecutionTimer(tempCtx, 300));    	
    	tempCtx = new SqlExecuteContext(parent);
    	TreeNode nodeSqlExec1= TreeNode.createTreeNode(getExecutionTimer(tempCtx, 150));                
        
        nodeApi1.addChild(nodeApi2);
        nodeApi2.addChild(nodeSqlPrep1);
        nodeApi2.addChild(nodeSqlExec1);
        //dummyNode.addChild(nodeApi1);
        
        Tree operTree = new Tree();
        //TreeNode dummyNode = TreeNode.createTreeNode("dummy root");
        operTree.setRoot(nodeApi1);
        
        
        

        // create another copy of nodeApi2 branch 
    	tempCtx = new ApiContext("com.business.package", "method3", "business");
    	TreeNode nodeApi2Dupe= TreeNode.createTreeNode(getExecutionTimer(tempCtx, 300));
    	
    	tempCtx = new SqlPrepareContext(parent);
    	TreeNode nodeSqlPrep1Dupe = TreeNode.createTreeNode(getExecutionTimer(tempCtx, 200));    	
    	tempCtx = new SqlExecuteContext(parent);
    	TreeNode nodeSqlExec1Dupe= TreeNode.createTreeNode(getExecutionTimer(tempCtx, 100));                
    	
        nodeApi2Dupe.addChild(nodeSqlPrep1Dupe);
        nodeApi2Dupe.addChild(nodeSqlExec1Dupe);
        //dummyNode.addChild(nodeApi2Dupe);
                        
        return operTree;    	
    }
    
        
    private Map createLayerTimesMap(String[] layers, long[] times) {
        Map layerTImeMap = new HashMap();
        int length = layers.length;
        
        for (int i = 0; i < length; i++) {
            LayerTime lt = new LayerTime(layers[i]);
            lt.setTime(times[i]);
            layerTImeMap.put(layers[i], lt);
        }
        return layerTImeMap;
    }
    
}