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
 * Original Author:  subin.p (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.web.util;

import java.util.List;

import junit.framework.TestCase;
import net.sf.infrared.aspects.jsp.JspContext;
import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.AggregateOperationTree;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

/**
 * @author subin.p
 *
 */
public class TreeUtilTest extends TestCase {
    public void testGetMergedExecutionContextTreeNodeForNullHierLayer() {        
    	AggregateOperationTree treeBefore = getAggregateOperationTree();                
        TreeNode result = TreeUtil.getMergedExecutionContextTreeNode(
               treeBefore, "com.em.Api1", "Jsp", "net.sf.infrared.aspects.jsp.JspContext", null  );
                
        AggregateExecutionTime aet = null;
        TreeNode treeNode = null;
        List children = null;
        List grandChildren = null;
        String temp = null;
        
        aet = (AggregateExecutionTime) result.getValue();
        assertEquals(3, aet.getExecutionCount());
        assertEquals("com.em.Api1", aet.getContext().getName());
                
        children = result.getChildren();        
        assertEquals(2, children.size());
                        
        for(int i = 0; i < 2; i++ ) {            
            treeNode = (TreeNode) children.get(i);
            aet = (AggregateExecutionTime) treeNode.getValue();
            temp = aet.getContext().getName();
            
            if("com.em.Api3".equals(temp)) {            
                assertEquals(2, aet.getExecutionCount());
                
                grandChildren = treeNode.getChildren();
                assertEquals(1, grandChildren.size());                                    
            }else if("com.em.Api4".equals(temp)) {
                assertEquals(1, aet.getExecutionCount());
                
                grandChildren = treeNode.getChildren();
                assertEquals(0, grandChildren.size());                                                            
            }else {
                fail();
            }            
        }        
    }
            


    public void testGetMergedExecutionContextTreeNodeForNotNullHierLayer() {        
        AggregateOperationTree treeBefore = getAggregateOperationTree();
        TreeNode result = TreeUtil.getMergedExecutionContextTreeNode(
                treeBefore, "com.em.Api1", "Jsp", "net.sf.infrared.aspects.jsp.JspContext", "web.jsp.mylayer"  );
        
        AggregateExecutionTime aet = null;
        TreeNode treeNode = null;
        List children = null;
        List grandChildren = null;
        String temp = null;
        
        aet = (AggregateExecutionTime) result.getValue();                
        assertEquals(2, aet.getExecutionCount());
        assertEquals("com.em.Api1", aet.getContext().getName());
                
        children = result.getChildren();        
        assertEquals(2, children.size());
                        
        for(int i = 0; i < 2; i++ ) {            
            treeNode = (TreeNode) children.get(i);
            aet = (AggregateExecutionTime) treeNode.getValue();
            temp = aet.getContext().getName();
            
            if("com.em.Api3".equals(temp)) {                            
                assertEquals(2, aet.getExecutionCount());
                
                grandChildren = treeNode.getChildren();
                assertEquals(1, grandChildren.size());                                    
            }else if("com.em.Api4".equals(temp)) {                
                assertEquals(1, aet.getExecutionCount());
                
                grandChildren = treeNode.getChildren();
                assertEquals(0, grandChildren.size());                                                            
            }else {
                fail();
            }            
        }
                        
        result = TreeUtil.getMergedExecutionContextTreeNode(
                treeBefore, "com.em.Api1", "Jsp", "net.sf.infrared.aspects.jsp.JspContext", "web.jsp"  );
                        
        aet = (AggregateExecutionTime) result.getValue();
        assertEquals(1, aet.getExecutionCount());
        assertEquals("com.em.Api1", aet.getContext().getName());
        assertEquals(0, result.getChildren().size());                 
    }
        
    private AggregateOperationTree getAggregateOperationTree() {
        long[] values = new long[] {1, 20, 11, 40, 22, 20, 40, 2, 4, 363636, 36363636, 84, 74};
        
//        ExecutionContext api1 = new ApiContext("com.em.Api1", "getIt", "Api");        
//        ExecutionContext api2 = new ApiContext("com.em.Api2", "getAllOfIt", "Api");
//        ExecutionContext api3 = new ApiContext("com.em.Api3", "enteyAllOfIt", "Api");
//        ExecutionContext api4 = new ApiContext("com.em.Api4", "hahaAllOfIt", "Api");
//        ExecutionContext api5 = new ApiContext("com.em.Api5", "mineAllOfIt", "Api");
//        ExecutionContext api6 = new ApiContext("com.em.Api6", "goodAllOfIt", "Api");
//        ExecutionContext api7 = new ApiContext("com.em.Api7", "hoopAllOfIt", "Api");
        
        ExecutionContext api1 = new JspContext("com.em.Api1");        
        ExecutionContext api2 = new JspContext("com.em.Api2");
        
        ExecutionContext api3 = new JspContext("com.em.Api3");
        ExecutionContext api4 = new JspContext("com.em.Api4");
        ExecutionContext api5 = new JspContext("com.em.Api5");
        ExecutionContext api6 = new JspContext("com.em.Api6");
        ExecutionContext api7 = new JspContext("com.em.Api7");
                
        AggregateExecutionTime aggExTime = getPopulatedAggExecTime(api1, values);
        aggExTime.setLayerName("web.jsp.mylayer");
        TreeNode nodeApi1 = TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(api1, values);
        aggExTime.setLayerName("web.jsp.mylayer");
        TreeNode nodeApi11 = TreeNode.createTreeNode(aggExTime);

        aggExTime = getPopulatedAggExecTime(api1, values);
        aggExTime.setLayerName("web.jsp");
        TreeNode nodeApi111 = TreeNode.createTreeNode(aggExTime);

                        
        aggExTime = getPopulatedAggExecTime(api2, values);
        TreeNode nodeApi2= TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(api2, values);
        TreeNode nodeApi22= TreeNode.createTreeNode(aggExTime);

        aggExTime = getPopulatedAggExecTime(api3, values);
        TreeNode nodeApi3= TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(api3, values);
        TreeNode nodeApi33= TreeNode.createTreeNode(aggExTime);

        aggExTime = getPopulatedAggExecTime(api4, values);
        TreeNode nodeApi4= TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(api4, values);
        TreeNode nodeApi44= TreeNode.createTreeNode(aggExTime);

        aggExTime = getPopulatedAggExecTime(api5, values);
        TreeNode nodeApi5= TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(api5, values);
        TreeNode nodeApi55= TreeNode.createTreeNode(aggExTime);


        aggExTime = getPopulatedAggExecTime(api6, values);
        TreeNode nodeApi6= TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(api7, values);
        TreeNode nodeApi7= TreeNode.createTreeNode(aggExTime);

        
        Tree aggregateTree = new Tree();
        TreeNode dummyNode = TreeNode.createTreeNode("dummy root");
        aggregateTree.setRoot(dummyNode);
        
        
        dummyNode.addChild(nodeApi1);
        dummyNode.addChild(nodeApi4);
        dummyNode.addChild(nodeApi5);
        
        nodeApi1.addChild(nodeApi3);
        nodeApi1.addChild(nodeApi44);
        
        nodeApi3.addChild(nodeApi55);
        
        nodeApi4.addChild(nodeApi11);
        nodeApi4.addChild(nodeApi2);
        
        nodeApi11.addChild(nodeApi33);
        
        nodeApi5.addChild(nodeApi6);
        nodeApi5.addChild(nodeApi7);
        
        nodeApi6.addChild(nodeApi111);
        nodeApi6.addChild(nodeApi22);
                
        AggregateOperationTree returnTree = new AggregateOperationTree();
        returnTree.setAggregateTree(aggregateTree);    

        return returnTree;
    }
        
    private AggregateExecutionTime getPopulatedAggExecTime(ExecutionContext ctx, long[] longVals) {
        AggregateExecutionTime aggExecTime = new AggregateExecutionTime(ctx);
        
        aggExecTime.setExecutionCount((int) longVals[0]);
        aggExecTime.setExclusiveFirstExecutionTime(longVals[1]);
        aggExecTime.setExclusiveLastExecutionTime(longVals[2]);
        aggExecTime.setInclusiveFirstExecutionTime(longVals[3]);
        aggExecTime.setInclusiveLastExecutionTime(longVals[4]);
        aggExecTime.setMaxExclusiveTime(longVals[5]);
        aggExecTime.setMaxInclusiveTime(longVals[6]);
        aggExecTime.setMinExclusiveTime(longVals[7]);
        aggExecTime.setMinInclusiveTime(longVals[8]);
        aggExecTime.setTimeOfFirstExecution(longVals[9]);
        aggExecTime.setTimeOfLastExecution(longVals[10]);
        aggExecTime.setTotalExclusiveTime(longVals[11]);
        aggExecTime.setTotalInclusiveTime(longVals[12]);
        return aggExecTime;        
    }

    
}
