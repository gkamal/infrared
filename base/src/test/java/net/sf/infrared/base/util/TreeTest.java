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

/**
 */
public class TreeTest extends TestCase {
    private Tree sampleTree;

    public TreeTest() {
        super("treeTest");
    }

    protected void setUp() {
        sampleTree = new Tree();
        TreeNode root = TreeNode.createTreeNode("0");
        sampleTree.setRoot(root);
        root.addChild(TreeNode.createTreeNode("0.0"));
        root.addChild(TreeNode.createTreeNode("0.1"));
        ((TreeNode) root.getChildren().get(0)).addChild(TreeNode.createTreeNode("0.0.0"));
        ((TreeNode) root.getChildren().get(0)).addChild(TreeNode.createTreeNode("0.0.1"));
    }

    public void testEmptyTree() {
        Tree tree = new Tree();
        assertTrue("root node of empty tree should be null", tree.getRoot() == null);
    }

    public void testTreeIntializedWithRootNode() {
        Tree tree = new Tree(TreeNode.createTreeNode("root"));
        assertTrue("Root node should be null", 
                tree.getRoot() != null && tree.getRoot().getValue().equals("root"));
    }

    public void testDepthPositionAttributes() {
        NodeVisitor depthPositionVerify = new NodeVisitor() {
            private int depth = 0;

            private int position = 0;

            public void visit(TreeNode node) {
                assertEquals("Depth incorrect", depth, node.getDepth());
                assertEquals("Position incorret", position, node.getPosition());
                position++;
            }

            public void goingDown() {
                depth++;
                position = 0;
            }

            public void climbingUp() {
                depth--;
            }

            public void beginTraversal() {
            }

            public void endTraversal() {
            }
        };

        sampleTree.traverseBreadthFirst(depthPositionVerify);
    }

    public void testFindForNode() {
        try {
            sampleTree.find(null);
        } catch (Exception e) {
            fail("Finding null shouldn't throw exception");
        }

        TreeNode foundNode = sampleTree.find("0.1");
        assertTrue("Error finding node with specific value", 
                foundNode != null && foundNode.getValue().equals("0.1"));

        assertNull("Should return null if it can't find a node", sampleTree.find("0.0.2"));
    }

    public void testFindInAnEmptyTree() {
        Tree tree = new Tree();
        try {
            assertNull("Find on empty tree should return null", tree.find("xyz"));
        } catch (Exception e) {
            fail("Invoking find on an empty tree should not throw exception");
        }

    }

    public void testFindMatchesRooNode() {
        TreeNode foundNode = sampleTree.find("0");
        assertTrue("Error finding node which is the root node", 
                foundNode != null && foundNode.getValue().equals("0"));
    }

    public void testTraverseBreadFirst() {

        NodeVisitor visitor = new NodeVisitor() {
            StringBuffer buf = new StringBuffer();

            int currentLevel = 0;

            int maxLevel = 0;

            public void visit(TreeNode node) {
                if (this.buf.length() > 0) {
                    this.buf.append("-");
                }
                this.buf.append((String) node.getValue());
            }

            public void goingDown() {
                currentLevel++;
                if (currentLevel > maxLevel) {
                    maxLevel = currentLevel;
                }
            }

            public void climbingUp() {
                currentLevel--;
                assertTrue("Level shouldn't become negative", currentLevel >= 0);
            }

            public void beginTraversal() {
            }

            public void endTraversal() {
                assertEquals("Breadth first traversal incorrect", new String(buf),
                        "0-0.0-0.1-0.0.0-0.0.1");
                assertEquals("Down method called incorrectly", maxLevel, 3);
                assertEquals("Up/Down method called incorreclty", currentLevel, 0);
            }
        };

        sampleTree.traverseBreadthFirst(visitor);
    }

    public void testResettingRoot() {
        TreeNode origRoot = TreeNode.createTreeNode("original root");
        TreeNode newRoot = TreeNode.createTreeNode("new root");

        Tree tree = new Tree(origRoot);

        assertEquals("Root's depth is not correct", 0, origRoot.getDepth());
        assertEquals("Root's position is not correct", 0, origRoot.getPosition());

        tree.setRoot(newRoot);

        assertEquals("Old root's depth is not correct", -1, origRoot.getDepth());
        assertEquals("Old root's position is not correct", -1, origRoot.getPosition());

        assertEquals("New root's depth is not correct", 0, newRoot.getDepth());
        assertEquals("New root's position is not correct", 0, newRoot.getPosition());
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(new TestSuite(TreeTest.class));
    }
}
