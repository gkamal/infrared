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
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.base.model;

import java.util.List;

import junit.framework.TestCase;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

public class ApplicationStatisticsTest extends TestCase {
    private static final String TEST_APP = "test-app";
    
    private static final String TEST_INST = "test-instance";
    
    public ApplicationStatisticsTest(String str) {
        super(str);
    }

    public void testMergeStatsOfADifferentApplication() {
        ApplicationStatistics fixture = new ApplicationStatistics(TEST_APP, TEST_INST);
        OperationStatistics stats = new OperationStatistics("not-" + TEST_APP, TEST_INST);
        try {
            // the app names are not the same, so an IllegalArgumentException shd be raised
            fixture.merge(stats);
            fail();
        } catch (IllegalArgumentException expected) {
        } catch (Throwable unexpected) {
            unexpected.printStackTrace();
            fail();
        }
        
        stats = new OperationStatistics(TEST_APP, "not-" + TEST_INST);
        try {
            // the instance ids are not the same, so an IllegalArgumentException shd be raised
            fixture.merge(stats);
            fail();
        } catch (IllegalArgumentException expected) {
        } catch (Throwable unexpected) {
            unexpected.printStackTrace();
            fail();
        }
    }
    
    public void testMergeNullOpStats(){
        // try merging a null operation statistics; shd not trip over
        ApplicationStatistics fixture = new ApplicationStatistics(TEST_APP, TEST_INST);
        OperationStatistics empty = null;
        fixture.merge(empty);
        assertFalse(fixture.hasStatistics());
    }
    
    public void testMergeEmptyOpStats() {
        // try merging an operation statistics with no stats in it; shd not trip over
        ApplicationStatistics fixture = new ApplicationStatistics(TEST_APP, TEST_INST);
        OperationStatistics empty = new OperationStatistics(TEST_APP, TEST_INST) {
            public String[] getLayers() {
                return new String[0];
            }
        };
        fixture.merge(empty);
        assertFalse(fixture.hasStatistics());
    }
    
    public void testAddingToLastInvocationTrees() {
        ApplicationStatistics fixture = new ApplicationStatistics(TEST_APP, TEST_INST);
        fixture.setMaxLastInvocations(2); // only two tree shd be held
        assertEquals("", 0, fixture.getLastInvocations().size()); // initially no data
        
        // add the first tree
        Tree tree1 = createTree("node1");
        fixture.addToLastInvocations(tree1);
        List out = fixture.getLastInvocations();
        assertEquals("", 1, out.size());
        assertTrue("", out.contains(tree1));
        
        // add a second tree
        Tree tree2 = createTree("node2");
        fixture.addToLastInvocations(tree2);
        out = fixture.getLastInvocations();
        assertEquals("", 2, out.size());
        assertTrue("", out.contains(tree2));
        assertTrue("", out.contains(tree1));
        
        // add a third one - this time the first tree shd be pushed out
        Tree tree3 = createTree("node3");
        fixture.addToLastInvocations(tree3);
        out = fixture.getLastInvocations();
        assertEquals("", 2, out.size());
        assertTrue("", out.contains(tree2));
        assertTrue("", out.contains(tree3));
        assertFalse("", out.contains(tree1)); // the first tree is dropped
    }

    private Tree createTree(String nodeName) {
        TreeNode node = TreeNode.createTreeNode(nodeName);
        return new Tree(node);
    }
}
