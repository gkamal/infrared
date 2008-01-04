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
 * Original Author:  binil.thomas (Tavant Technologies)
 * Contributor(s):   prashant.nair, subin.p;
 *
 */
package net.sf.infrared.base.model;

import java.util.List;

import junit.framework.TestCase;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

import org.easymock.MockControl;

public class AggregateOperationTreeTest extends TestCase {
    /* 
     * No tree exists originally
     *
     * New tree is
     * A
     * --- B
     * ---- D
     * --- C
     *
     * Merged tree must be
     * A
     * --- B
     * ---- D
     * --- C
     */
    public void testForFirstTime() {
        AggregateOperationTree fixture = new AggregateOperationTree();

        ExecutionContext a = createMockContext();
        ExecutionContext b = createMockContext();
        ExecutionContext c = createMockContext();
        ExecutionContext d = createMockContext();

        ExecutionTimer etA = createTimer(a, 10, 10);
        ExecutionTimer etB = createTimer(b, 10, 10);
        ExecutionTimer etC = createTimer(c, 10, 10);
        ExecutionTimer etD = createTimer(d, 10, 10);

        TreeNode nodeA = TreeNode.createTreeNode(etA);
        TreeNode nodeB = TreeNode.createTreeNode(etB);
        TreeNode nodeC = TreeNode.createTreeNode(etC);
        TreeNode nodeD = TreeNode.createTreeNode(etD);

        Tree testOpTree = new Tree();
        testOpTree.setRoot(nodeA);
        nodeA.addChild(nodeB);
        nodeA.addChild(nodeC);
        nodeB.addChild(nodeD);

        fixture.merge(testOpTree);

        Tree aggTree = fixture.getAggregateTree();
        assertEquals("", 1, aggTree.getRoot().getChildren().size());

        TreeNode aggNodeA = (TreeNode) aggTree.getRoot().getChildren().get(0);
        assertEquals("", a, ((AggregateExecutionTime) aggNodeA.getValue()).getContext());
        assertEquals("", 2, aggNodeA.getChildren().size());

        TreeNode aggNodeB = (TreeNode) aggNodeA.getChildren().get(0);
        assertEquals("", b, ((AggregateExecutionTime) aggNodeB.getValue()).getContext());
        assertEquals("", 1, aggNodeB.getChildren().size());

        TreeNode aggNodeC = (TreeNode) aggNodeA.getChildren().get(1);
        assertEquals("", c, ((AggregateExecutionTime) aggNodeC.getValue()).getContext());
        assertEquals("", 0, aggNodeC.getChildren().size());

        TreeNode aggNodeD = (TreeNode) aggNodeB.getChildren().get(0);
        assertEquals("", d, ((AggregateExecutionTime) aggNodeD.getValue()).getContext());
        assertEquals("", 0, aggNodeD.getChildren().size());
    }


 /*   public void testAggOperTreeMergeOnItself() {

        AggregateOperationTree opTree = getOneAggregateOperationTree();
        opTree.merge(getOneAggregateOperationTree());
        
        TreeNode treeNode = (TreeNode) opTree.getAggregateTree().getRoot().getChildren().get(0);
        TreeNode secondLevelTreeNode = (TreeNode) treeNode.getChildren().get(0);
        
        AggregateExecutionTime aggExecTim = (AggregateExecutionTime) secondLevelTreeNode.getValue();
        assertEquals(6, aggExecTim.getExecutionCount());
    }  */
    
    
    
    private ExecutionContext createMockContext() {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        return (ExecutionContext) ctrl.getMock();
    }

    private ExecutionTimer createTimer(final ExecutionContext ex, final long exTime,
            final long inTime) {
        return new ExecutionTimer(ex) {
            public long getExclusiveTime() {
                return exTime;
            }

            public long getInclusiveTime() {
                return inTime;
            }
        };
    }

    /*
     * Existing tree is
     * A
     * --- B
     * ---- D
     * --- C
     *
     * New tree is
     * A
     * --- B
     * --- E
     *
     * Resulting merge tree should be
     * A (2)
     * -- B (2)
     * -- D
     * -- C
     * -- E
     *
     */
    public void testMergeWithNoDups() {
        AggregateOperationTree fixture = new AggregateOperationTree();

        ExecutionContext a = createMockContext();
        ExecutionContext b = createMockContext();
        ExecutionContext c = createMockContext();
        ExecutionContext d = createMockContext();
        ExecutionContext e = createMockContext();

        ExecutionTimer etA = createTimer(a, 10, 10);
        ExecutionTimer etB = createTimer(b, 10, 10);
        ExecutionTimer etC = createTimer(c, 10, 10);
        ExecutionTimer etD = createTimer(d, 10, 10);
        ExecutionTimer etE = createTimer(e, 10, 10);

        TreeNode nodeA1 = TreeNode.createTreeNode(etA);
        TreeNode nodeB1 = TreeNode.createTreeNode(etB);
        TreeNode nodeC1 = TreeNode.createTreeNode(etC);
        TreeNode nodeD1 = TreeNode.createTreeNode(etD);

        Tree t = new Tree();
        t.setRoot(nodeA1);
        nodeA1.addChild(nodeB1);
        nodeA1.addChild(nodeC1);
        nodeB1.addChild(nodeD1);

        fixture.merge(t);

        TreeNode nodeA2 = TreeNode.createTreeNode(etA);
        TreeNode nodeB2 = TreeNode.createTreeNode(etB);
        TreeNode nodeE2 = TreeNode.createTreeNode(etE);

        Tree testOpTree = new Tree();
        testOpTree.setRoot(nodeA2);
        nodeA2.addChild(nodeB2);
        nodeA2.addChild(nodeE2);

        fixture.merge(testOpTree);

        Tree aggTree = fixture.getAggregateTree();
        assertEquals("", 1, aggTree.getRoot().getChildren().size());

        TreeNode aggNodeA = (TreeNode) aggTree.getRoot().getChildren().get(0);
        AggregateExecutionTime aggExA = (AggregateExecutionTime) aggNodeA.getValue();
        assertEquals("", a, aggExA.getContext());
        assertEquals("", 3, aggNodeA.getChildren().size());
        assertEquals("", 2, aggExA.getExecutionCount());

        TreeNode aggNodeB = (TreeNode) aggNodeA.getChildren().get(0);
        AggregateExecutionTime aggExB = (AggregateExecutionTime) aggNodeB.getValue();
        assertEquals("", b, aggExB.getContext());
        assertEquals("", 1, aggNodeB.getChildren().size());
        assertEquals("", 2, aggExB.getExecutionCount());

        TreeNode aggNodeC = (TreeNode) aggNodeA.getChildren().get(1);
        AggregateExecutionTime aggExC = (AggregateExecutionTime) aggNodeC.getValue();
        assertEquals("", c, aggExC.getContext());
        assertEquals("", 0, aggNodeC.getChildren().size());
        assertEquals("", 1, aggExC.getExecutionCount());

        TreeNode aggNodeE = (TreeNode) aggNodeA.getChildren().get(2);
        AggregateExecutionTime aggExE = (AggregateExecutionTime) aggNodeE.getValue();
        assertEquals("", e, aggExE.getContext());
        assertEquals("", 0, aggNodeE.getChildren().size());
        assertEquals("", 1, aggExE.getExecutionCount());

        TreeNode aggNodeD = (TreeNode) aggNodeB.getChildren().get(0);
        AggregateExecutionTime aggExD = (AggregateExecutionTime) aggNodeD.getValue();
        assertEquals("", d, aggExD.getContext());
        assertEquals("", 0, aggNodeD.getChildren().size());
        assertEquals("", 1, aggExD.getExecutionCount());
    }

    /*
     * Existing tree is
     * A
     * --- B
     * ---- D
     * --- C
     *
     * New tree is
     * A
     * --- B
     * --- B
     * --- E
     * --- B
     *
     * Resulting merge tree should be
     * A (2)
     * -- B (3)
     * -- D
     * -- C
     * -- E
     * --- B
     *
     */
    public void testMergeWithDups() {
        AggregateOperationTree fixture = new AggregateOperationTree();

        ExecutionContext a = createMockContext();
        ExecutionContext b = createMockContext();
        ExecutionContext c = createMockContext();
        ExecutionContext d = createMockContext();
        ExecutionContext e = createMockContext();

        ExecutionTimer etA = createTimer(a, 10, 10);
        ExecutionTimer etB = createTimer(b, 10, 10);
        ExecutionTimer etC = createTimer(c, 10, 10);
        ExecutionTimer etD = createTimer(d, 10, 10);
        ExecutionTimer etE = createTimer(e, 10, 10);

        TreeNode nodeA1 = TreeNode.createTreeNode(etA);
        TreeNode nodeB1 = TreeNode.createTreeNode(etB);
        TreeNode nodeC1 = TreeNode.createTreeNode(etC);
        TreeNode nodeD1 = TreeNode.createTreeNode(etD);

        // nodeA1
        // +--- nodeB1
        // +---- nodeD1
        // +--- nodeC1
        Tree t = new Tree();
        t.setRoot(nodeA1);
        nodeA1.addChild(nodeB1);
        nodeA1.addChild(nodeC1);
        nodeB1.addChild(nodeD1);

        fixture.merge(t);

        TreeNode nodeA2 = TreeNode.createTreeNode(etA);
        TreeNode nodeB21 = TreeNode.createTreeNode(etB);
        TreeNode nodeB22 = TreeNode.createTreeNode(etB);
        TreeNode nodeB23 = TreeNode.createTreeNode(etB);
        TreeNode nodeE2 = TreeNode.createTreeNode(etE);

        // nodeA2
        // +--- nodeB21
        // +--- nodeB22
        // +--- nodeE2
        // +--- nodeB23
        Tree testOpTree = new Tree();
        testOpTree.setRoot(nodeA2);
        nodeA2.addChild(nodeB21);
        nodeA2.addChild(nodeB22);
        nodeA2.addChild(nodeE2);
        nodeE2.addChild(nodeB23);

        fixture.merge(testOpTree);

        // A (2)
        // -- B (3)
        // -- D (1)
        // -- C (1)
        // -- E (1)
        // --- B (1)
        Tree aggTree = fixture.getAggregateTree();
        assertEquals("", 1, aggTree.getRoot().getChildren().size());

        TreeNode aggNodeA = (TreeNode) aggTree.getRoot().getChildren().get(0);
        AggregateExecutionTime aggExA = (AggregateExecutionTime) aggNodeA.getValue();
        assertEquals("", a, aggExA.getContext());
        assertEquals("", 3, aggNodeA.getChildren().size());
        assertEquals("", 2, aggExA.getExecutionCount());

        TreeNode aggNodeB1 = (TreeNode) aggNodeA.getChildren().get(0);
        AggregateExecutionTime aggExB1 = (AggregateExecutionTime) aggNodeB1.getValue();
        assertEquals("", b, aggExB1.getContext());
        assertEquals("", 1, aggNodeB1.getChildren().size());
        assertEquals("", 3, aggExB1.getExecutionCount());

        TreeNode aggNodeC = (TreeNode) aggNodeA.getChildren().get(1);
        AggregateExecutionTime aggExC = (AggregateExecutionTime) aggNodeC.getValue();
        assertEquals("", c, aggExC.getContext());
        assertEquals("", 0, aggNodeC.getChildren().size());
        assertEquals("", 1, aggExC.getExecutionCount());

        TreeNode aggNodeE = (TreeNode) aggNodeA.getChildren().get(2);
        AggregateExecutionTime aggExE = (AggregateExecutionTime) aggNodeE.getValue();
        assertEquals("", e, aggExE.getContext());
        assertEquals("", 1, aggNodeE.getChildren().size());
        assertEquals("", 1, aggExE.getExecutionCount());

        TreeNode aggNodeD = (TreeNode) aggNodeB1.getChildren().get(0);
        AggregateExecutionTime aggExD = (AggregateExecutionTime) aggNodeD.getValue();
        assertEquals("", d, aggExD.getContext());
        assertEquals("", 0, aggNodeD.getChildren().size());
        assertEquals("", 1, aggExD.getExecutionCount());

        TreeNode aggNodeB2 = (TreeNode) aggNodeE.getChildren().get(0);
        AggregateExecutionTime aggExB2 = (AggregateExecutionTime) aggNodeB2.getValue();
        assertEquals("", b, aggExB2.getContext());
        assertEquals("", 0, aggNodeB2.getChildren().size());
        assertEquals("", 1, aggExB2.getExecutionCount());
    }

    /*
     * Existing Tree
     * A
     * --B
     *
     *
     * Tree to merge
     * A
     * -- B
     * -- C
     * -- D
     * -- D
     * -- E
     * -- D
     *
     * Resulting Tree
     * A (2)
     * -- B (2)
     * -- C (1)
     * -- D (3)
     * -- E (1)
     */
    public void testMergeWhenNewBranchHasDups() {
        AggregateOperationTree fixture = new AggregateOperationTree();

        ExecutionContext a = createMockContext();
        ExecutionContext b = createMockContext();

        ExecutionTimer etA = createTimer(a, 10, 10);
        ExecutionTimer etB = createTimer(b, 10, 10);

        TreeNode nodeA1 = TreeNode.createTreeNode(etA);
        TreeNode nodeB1 = TreeNode.createTreeNode(etB);

        // nodeA1
        // +--- nodeB1
        Tree t = new Tree();
        t.setRoot(nodeA1);
        nodeA1.addChild(nodeB1);

        fixture.merge(t);

        ExecutionContext c = createMockContext();
        ExecutionContext d = createMockContext();
        ExecutionContext e = createMockContext();

        ExecutionTimer etC = createTimer(c, 10, 10);
        ExecutionTimer etD = createTimer(d, 10, 10);
        ExecutionTimer etE = createTimer(e, 10, 10);

        TreeNode nodeA2 = TreeNode.createTreeNode(etA);
        TreeNode nodeB2 = TreeNode.createTreeNode(etB);
        TreeNode nodeC2 = TreeNode.createTreeNode(etC);
        TreeNode nodeD21 = TreeNode.createTreeNode(etD);
        TreeNode nodeD22 = TreeNode.createTreeNode(etD);
        TreeNode nodeD23 = TreeNode.createTreeNode(etD);
        TreeNode nodeE2 = TreeNode.createTreeNode(etE);

        // nodeA2
        // +--- nodeB2
        // +---- nodeC2
        // +---- nodeD21
        // +---- nodeD22
        // +---- nodeE2
        // +---- nodeD23
        Tree testOpTree = new Tree();
        testOpTree.setRoot(nodeA2);
        nodeA2.addChild(nodeB2);
        nodeB2.addChild(nodeC2);
        nodeC2.addChild(nodeD21);
        nodeC2.addChild(nodeD22);
        nodeC2.addChild(nodeD23);
        nodeD22.addChild(nodeE2);

        fixture.merge(testOpTree);

        // We expect:
        // A (2)
        // -- B (2)
        // -- C (1)
        // -- D (3)
        // -- E (1)
        Tree aggTree = fixture.getAggregateTree();
        assertEquals("", 1, aggTree.getRoot().getChildren().size());

        TreeNode aggNodeA = (TreeNode) aggTree.getRoot().getChildren().get(0);
        AggregateExecutionTime aggExA = (AggregateExecutionTime) aggNodeA.getValue();
        assertEquals("", a, aggExA.getContext());
        assertEquals("", 1, aggNodeA.getChildren().size());
        assertEquals("", 2, aggExA.getExecutionCount());

        TreeNode aggNodeB = (TreeNode) aggNodeA.getChildren().get(0);
        AggregateExecutionTime aggExB = (AggregateExecutionTime) aggNodeB.getValue();
        assertEquals("", b, aggExB.getContext());
        assertEquals("", 1, aggNodeB.getChildren().size());
        assertEquals("", 2, aggExB.getExecutionCount());

        TreeNode aggNodeC = (TreeNode) aggNodeB.getChildren().get(0);
        AggregateExecutionTime aggExC = (AggregateExecutionTime) aggNodeC.getValue();
        assertEquals("", c, aggExC.getContext());
        assertEquals("", 1, aggNodeC.getChildren().size());
        assertEquals("", 1, aggExC.getExecutionCount());

        TreeNode aggNodeD = (TreeNode) aggNodeC.getChildren().get(0);
        AggregateExecutionTime aggExD = (AggregateExecutionTime) aggNodeD.getValue();
        assertEquals("", d, aggExD.getContext());
        assertEquals("", 1, aggNodeD.getChildren().size());
        assertEquals("", 3, aggExD.getExecutionCount());

        TreeNode aggNodeE = (TreeNode) aggNodeD.getChildren().get(0);
        AggregateExecutionTime aggExE = (AggregateExecutionTime) aggNodeE.getValue();
        assertEquals("", e, aggExE.getContext());
        assertEquals("", 0, aggNodeE.getChildren().size());
        assertEquals("", 1, aggExE.getExecutionCount());
    }

    /*
     * Tree 1
     *
     * dummy
     * --A
     * ----B
     * ------C
     *
     * Tree 2
     *
     * dummy
     * --D
     * ----E
     * ------F
     *
     * After Merge
     *
     * dummy
     * --A
     * ----B
     * ------C
     * --D
     * ----E
     * ------F
     */
    public void testAggrOpTreeMergeWithDiffRoots() {
        AggregateOperationTree opTree1 = new AggregateOperationTree();

        ExecutionContext a = createMockContext();
        ExecutionContext b = createMockContext();
        ExecutionContext c = createMockContext();

        ExecutionTimer etA = createTimer(a, 10, 10);
        ExecutionTimer etB = createTimer(b, 10, 10);
        ExecutionTimer etC = createTimer(c, 10, 10);

        TreeNode nodeA = TreeNode.createTreeNode(etA);
        TreeNode nodeB = TreeNode.createTreeNode(etB);
        TreeNode nodeC = TreeNode.createTreeNode(etC);

        Tree testOpTree1 = new Tree();
        testOpTree1.setRoot(nodeA);
        nodeA.addChild(nodeB);
        nodeA.addChild(nodeC);

        opTree1.merge(testOpTree1);

        AggregateOperationTree opTree2 = new AggregateOperationTree();

        ExecutionContext d = createMockContext();
        ExecutionContext e = createMockContext();
        ExecutionContext f = createMockContext();

        ExecutionTimer etD = createTimer(d, 10, 10);
        ExecutionTimer etE = createTimer(e, 10, 10);
        ExecutionTimer etF = createTimer(f, 10, 10);

        TreeNode nodeD = TreeNode.createTreeNode(etD);
        TreeNode nodeE = TreeNode.createTreeNode(etE);
        TreeNode nodeF = TreeNode.createTreeNode(etF);

        Tree testOpTree2 = new Tree();
        testOpTree2.setRoot(nodeD);
        nodeD.addChild(nodeE);
        nodeD.addChild(nodeF);

        opTree2.merge(testOpTree2);

        opTree1.merge(opTree2);

        assertEquals(2, opTree1.getAggregateTree().getRoot().getChildren().size());

        List children = opTree1.getAggregateTree().getRoot().getChildren();

        TreeNode firstNode = (TreeNode) children.get(0);
        assertEquals(a, ((AggregateExecutionTime) firstNode.getValue()).getContext());

        TreeNode secondNode = (TreeNode) children.get(1);
        assertEquals(d, ((AggregateExecutionTime) secondNode.getValue()).getContext());

        TreeNode thirdNode = (TreeNode) secondNode.getChildren().get(0);
        assertEquals(e, ((AggregateExecutionTime) thirdNode.getValue()).getContext());

    }

    /*
     * Tree 1
     * dummy
     * --A
     * ----B
     * ----C
     * --D
     *
     * Tree 2
     * dummy
     * --A
     * ----B
     * ----D
     *
     * Merged Tree
     *
     * dummy
     * --A
     * ----B
     * ----C
     * ----D
     * --D
     */
    public void testMergeAggrOpTreeWithMutlipleChildNodes() {
        AggregateOperationTree opTree1 = new AggregateOperationTree();

        ExecutionContext a = createMockContext();
        ExecutionContext b = createMockContext();
        ExecutionContext c = createMockContext();
        ExecutionContext d = createMockContext();

        ExecutionTimer etA = createTimer(a, 10, 10);
        ExecutionTimer etB = createTimer(b, 5, 5);
        ExecutionTimer etC = createTimer(c, 10, 10);
        ExecutionTimer etD = createTimer(d, 10, 10);

        TreeNode nodeA1 = TreeNode.createTreeNode(etA);
        TreeNode nodeB1 = TreeNode.createTreeNode(etB);
        TreeNode nodeC1 = TreeNode.createTreeNode(etC);
        TreeNode nodeD1 = TreeNode.createTreeNode(etD);

        Tree testOpTree1 = new Tree();
        testOpTree1.setRoot(nodeA1);
        nodeA1.addChild(nodeB1);
        nodeA1.addChild(nodeC1);

        Tree testOpTree2 = new Tree();
        testOpTree2.setRoot(nodeD1);

        opTree1.merge(testOpTree1);
        opTree1.merge(testOpTree2);

        AggregateOperationTree opTree2 = new AggregateOperationTree();

        TreeNode nodeA2 = TreeNode.createTreeNode(etA);
        TreeNode nodeB2 = TreeNode.createTreeNode(etB);
        TreeNode nodeD2 = TreeNode.createTreeNode(etD);

        Tree testOpTree3 = new Tree();
        testOpTree3.setRoot(nodeA2);
        nodeA2.addChild(nodeB2);
        nodeA2.addChild(nodeD2);

        opTree2.merge(testOpTree3);

        opTree1.merge(testOpTree3);

        List children = opTree1.getAggregateTree().getRoot().getChildren();

        TreeNode firstNode = (TreeNode) children.get(0);
        assertEquals(2, ((AggregateExecutionTime) firstNode.getValue()).getExecutionCount());
        assertEquals(20, ((AggregateExecutionTime) firstNode.getValue()).getTotalInclusiveTime());

        assertEquals(3, firstNode.getChildren().size());

        TreeNode secondNode = (TreeNode) firstNode.getChildren().get(0);
        assertEquals(2, ((AggregateExecutionTime) secondNode.getValue()).getExecutionCount());
        assertEquals(10, ((AggregateExecutionTime) secondNode.getValue()).getTotalInclusiveTime());
    }

    
    public AggregateOperationTree getOneAggregateOperationTree() {
        
    
        // This method returns the tree below.
        
        //dummy        
        //    +---  api1
        //             +--- api2
        //                     +---sqlPrep1                      
        //                     +---sqlExec1
        
        //    +---  aggExTime 
        //             +---aggExTime                      
        //             +---aggExTime
        
        long[] values = new long[] {3, 20, 11, 40, 22, 20, 40, 2, 4, 363636, 36363636, 84, 74};
        
        ExecutionContext api1 = createMockContext();       
        ExecutionContext api2 = createMockContext();
                
        ExecutionContext sqlPrep1 = createMockContext();
        ExecutionContext sqlExec1 = createMockContext();
        
        AggregateExecutionTime aggExTime = getPopulatedAggExecTime(api1, values);                        
        TreeNode nodeApi1 = TreeNode.createTreeNode(aggExTime);
                
        aggExTime = getPopulatedAggExecTime(api2, values);
        TreeNode nodeApi2= TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(sqlPrep1, values);
        TreeNode nodeSqlPrep1 = TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(sqlExec1, values);
        TreeNode nodeSqlExec1 = TreeNode.createTreeNode(aggExTime);

                       

        aggExTime = getPopulatedAggExecTime(api2, values);
        TreeNode nodeApi2Dupe= TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(sqlPrep1, values);
        TreeNode nodeSqlPrep1Dupe = TreeNode.createTreeNode(aggExTime);
        
        aggExTime = getPopulatedAggExecTime(sqlExec1, values);
        TreeNode nodeSqlExec1Dupe = TreeNode.createTreeNode(aggExTime);
  
        
        Tree aggregateTree = new Tree();
        TreeNode dummyNode = TreeNode.createTreeNode("dummy root");
        aggregateTree.setRoot(dummyNode);
                           
        nodeApi1.addChild(nodeApi2);
        nodeApi2.addChild(nodeSqlPrep1);
        nodeApi2.addChild(nodeSqlExec1);
        dummyNode.addChild(nodeApi1);
        
        nodeApi2Dupe.addChild(nodeSqlPrep1Dupe);
        nodeApi2Dupe.addChild(nodeSqlExec1Dupe);
        dummyNode.addChild(nodeApi2Dupe);
                
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
    
    
    public void testNonMerge() {

    }
}
