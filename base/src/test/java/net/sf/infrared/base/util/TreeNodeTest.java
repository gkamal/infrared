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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.base.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TreeNodeTest extends TestCase {
    public TreeNodeTest(String name) {
        super(name);
    }

    public void testNodeWithNullValue() {
        TreeNode node = TreeNode.createTreeNode(null);
        assertTrue("node value should be null", node.getValue() == null);
    }

    public void testNodeWithSimpleValue() {
        String value = new String("value");
        TreeNode node = TreeNode.createTreeNode(value);
        assertEquals("Value stored in the node should be same as what was set", node.getValue(),
                value);
        assertTrue("Depth & position should be -1 when node not part of tree",
                node.getDepth() == -1 && node.getPosition() == -1);
    }

    public void testNodeWithoutAnyChildNodes() {
        TreeNode node = TreeNode.createTreeNode(new String("testNode"));
        assertTrue("Shouldn't have any children", node.getChildren().size() == 0);
    }

    public void testNodeWithSomeChildNodes() {
        TreeNode node = TreeNode.createTreeNode(new String("testNode"));
        TreeNode firstChild = TreeNode.createTreeNode(new String("firstChild"));
        node.addChild(firstChild);
        assertTrue("Node should have one child", node.getChildren().size() == 1);
        assertTrue("We should get back the node we added", node.getChildren().get(0).equals(
                firstChild));
        assertTrue("Parent node should be set correclty", 
                firstChild.getParent() != null && firstChild.getParent().equals(node));

        try {
            node.addChild(null);
            fail("Shouldn't allow adding null as child nodes ");
        } catch (IllegalArgumentException e) {
        }

        assertTrue("null shouldn't be added", node.getChildren().size() == 1);
    }

    public void testRemoveChildFromNode() {
        TreeNode node = TreeNode.createTreeNode(new String("testNode"));
        TreeNode child = TreeNode.createTreeNode(new String("childNode"));
        node.addChild(child);
        assertTrue("Parent node should be set correclty", child.getParent() != null
                && child.getParent().equals(node));
        node.removeChild(child);
        assertTrue("Child node should have been removed", node.getChildren().size() == 0);
        assertTrue("After removing parent should be reset to null", child.getParent() == null);
        assertTrue("After removing depth & position should be reset", 
                child.getDepth() == -1 && child.getPosition() == -1);
    }

    public void testRemoveChildWithChildrenFromNode() {
        TreeNode node = TreeNode.createTreeNode(new String("testNode"));
        TreeNode child = TreeNode.createTreeNode(new String("childNode"));
        node.addChild(child);
        node.addChild(TreeNode.createTreeNode(new String("anotherChildNode")));
        child.addChild(TreeNode.createTreeNode(new String("childs-childNode")));
        node.removeChild(child);
        assertTrue("Child node should have been removed", node.getChildren().size() == 1);
        assertEquals("Depth of removed node rest", -1, child.getDepth());
        assertEquals("Position of removed node reset", -1, child.getPosition());
        TreeNode anotherChild = node.find("anotherChildNode");
        assertTrue("Position attribute of second child adjusted", anotherChild.getPosition() == 0);
        assertTrue("Depth of childs-childNode modified",
                child.find("childs-childNode").getDepth() == -1);
    }

    public void testRemoveNullChildFromNode() {
        TreeNode node = TreeNode.createTreeNode(new String("testNode"));
        TreeNode child = TreeNode.createTreeNode(new String("childNode"));
        node.addChild(child);
        try {
            node.removeChild(null);
            fail("Trying to remove NULL node should throw an exception");
        } catch (IllegalArgumentException e) {
        }
        assertTrue("Trying to remove null child should alter node", node.getChildren().size() == 1);
    }

    public void testRemoveNonExistentChild() {
        TreeNode node = TreeNode.createTreeNode(new String("testNode"));
        TreeNode child = TreeNode.createTreeNode(new String("childNode"));
        node.addChild(child);
        try {
            assertFalse("Removing non-existent children should fail", 
                    node.removeChild(TreeNode.createTreeNode("childNode1")));
        } catch (Exception e) {
            fail("Removing non-existent children shouldn't throw exception");
        }

        assertFalse("This should fail - remove does reference compare and not value", 
                node.removeChild(TreeNode.createTreeNode("childNode")));
    }

    public void testFindForMissingNode() {
        TreeNode node = TreeNode.createTreeNode(new String("testNode"));
        TreeNode child = TreeNode.createTreeNode(new String("childNode"));
        node.addChild(child);
        assertNull("Find for missing node should return NULL", node.find("CHILDNODE"));
        try {
            node.find(null);
        } catch (Exception e) {
            fail("Finding null object shouldn't throw exception");
        }
    }

    public void testFindNode() {
        TreeNode node = TreeNode.createTreeNode(new String("testNode"));
        TreeNode child = TreeNode.createTreeNode(new String("childNode"));
        node.addChild(child);
        child.addChild(TreeNode.createTreeNode(new String("childs-childNode")));
        TreeNode foundNode = node.find("childs-childNode");
        assertTrue("Error finding node which is not directly a child of node", 
                foundNode != null && foundNode.getValue().equals("childs-childNode"));
    }

    public void testAddNodeAsChildrenOfTwoNodes() {
        TreeNode node1 = TreeNode.createTreeNode(new String("testNode1"));
        TreeNode node2 = TreeNode.createTreeNode(new String("testNode2"));
        TreeNode childNode = TreeNode.createTreeNode(new String("childNode"));

        node1.addChild(childNode);

        try {
            node2.addChild(childNode);
            fail("Shouldn't allow a node to be added as child of two nodes");
        } catch (IllegalArgumentException e) {
        }

        node1.removeChild(childNode);

        try {
            node2.addChild(childNode);
        } catch (IllegalArgumentException e) {
            fail("Should allow adding as child of another node after removing from one node");
        }
    }

    public void testSetDepth() {
        int testDepth = 3;

        TreeNode parent = TreeNode.createTreeNode("parent");
        TreeNode child1 = TreeNode.createTreeNode("child-1");
        TreeNode child2 = TreeNode.createTreeNode("child-2");

        parent.addChild(child1);
        parent.addChild(child2);

        parent.setDepth(testDepth);

        assertEquals("setting parents depth should change depth of child", testDepth + 1, 
                child1.getDepth());
    }
    
    public void testMergeASingleChild() {
        TreeNode fixture = createMergeFixture();
        
        TreeNode child = TreeNode.createTreeNode("B1");
        
        fixture.mergeAsChild(child, new TestMerger());
        
        assertEquals(3, fixture.getChildren().size());
    }
    
    public void testMergeASingleNewChild() {
        TreeNode fixture = createMergeFixture();
        
        TreeNode child = TreeNode.createTreeNode("D");
        
        fixture.mergeAsChild(child, new TestMerger());
        
        assertEquals(4, fixture.getChildren().size());
    }
           
    public void testMergeAsChild1() {
        TreeNode fixture = createMergeFixture();
        
        // B2
        //  ---- C1
        TreeNode node1 = TreeNode.createTreeNode("B2");
        TreeNode node2 = TreeNode.createTreeNode("C1");
        node1.addChild(node2);
        
        fixture.mergeAsChild(node1, new TestMerger());
                       
        assertEquals(3, fixture.getChildren().size());
                        
        // C1 is added as B2's child; should not be merged with other C1 under B1
        assertEquals(4, fixture.find("B2-merged").getChildren().size());                                                                                                                            
    }
        
    public void testMergeAsChild2() {
        TreeNode fixture = createMergeFixture();
        
        // B4
        //  ---- C6
        //        ---- D1
        TreeNode node1 = TreeNode.createTreeNode("B4");
        TreeNode node2 = TreeNode.createTreeNode("C6");
        TreeNode node3 = TreeNode.createTreeNode("D1");
        node1.addChild(node2);
        node2.addChild(node3);
        
        fixture.mergeAsChild(node1, new TestMerger());
        
        assertEquals(4, fixture.getChildren().size());
                
        assertEquals(1, fixture.find("B4").getChildren().size());                                                                                                                            
    }

    // This method creates the fixture for tests for mergeAsChild() method
    // The tree created is:
    //   A
    //   ---- B1
    //        ---- C1
    //        ---- C2
    //   ---- B2
    //        ---- C3
    //        ---- C4
    //        ---- C5
    //   ---- B3    
    private TreeNode createMergeFixture() {        
        TreeNode fixture = TreeNode.createTreeNode("A");
        TreeNode child1 = TreeNode.createTreeNode("B1");
        TreeNode child2 = TreeNode.createTreeNode("B2");
        TreeNode child3 = TreeNode.createTreeNode("B3");
        TreeNode grandChild1 = TreeNode.createTreeNode("C1");
        TreeNode grandChild2 = TreeNode.createTreeNode("C2");
        TreeNode grandChild3 = TreeNode.createTreeNode("C3");
        TreeNode grandChild4 = TreeNode.createTreeNode("C4");
        TreeNode grandChild5 = TreeNode.createTreeNode("C5");
        
        fixture.addChild(child1);
        fixture.addChild(child2);
        fixture.addChild(child3);
        child1.addChild(grandChild1);
        child1.addChild(grandChild2);
        child2.addChild(grandChild3);
        child2.addChild(grandChild4);
        child2.addChild(grandChild5);
        
        return fixture;
    }
    
    class TestMerger implements Merger {
        public TreeNode createNewNode(TreeNode from) {
            return TreeNode.createTreeNode(from.getValue());
        }

        public void mergeValue(TreeNode mergeOnto, TreeNode toMerge) {
            if ( ! mergeOnto.getValue().equals( toMerge.getValue()) ) {
                throw new RuntimeException("test failure");
            }            
            String s = (String) mergeOnto.getValue();
            mergeOnto.setValue(s + "-merged");
        }

        public boolean isMatching(TreeNode original, TreeNode toMerge) {
            return original.getValue().equals( toMerge.getValue() );
        }        
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(TreeNodeTest.class));
    }
}
