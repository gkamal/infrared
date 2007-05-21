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
package net.sf.infrared.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import net.sf.infrared.agent.transport.CollectionStrategy;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

/**
 *
 * @author binil.thomas
 */
public class AgentTest extends TestCase {
    // @TODO huge method, break it up into smaller understandable chunks
    // debatable - maybe a test is more understandable if it is listed one after the other like this :-)
    public void testIfAgentRecordsStatsOfAnOperationCorrectly() {
        /*
         * A.a1 (1000)
         *   +-- A.a2 (800)
         *   |     +-- B.b1 (300)
         *   |     |    +-- C.c1 (200)
         *   |     |
         *   |     +-- B.b2 (200)
         *   |     |    +-- A.a3 (100)
         *   |     |
         *   |     +-- C.c2 (100)
         *   |
         *   +-- C.c3 (100)
         *         +-- D.d1 (5)
         *         |
         *         +-- D.d2 (20)
         *         |
         *         +-- E.e1 (5)
         */
        
        // set up a fixture StatisticsCollector
        // monitoring is enabled, call tracing is enabled, prune time is 10ms.
        final TestCollectionStrategy testCol = new TestCollectionStrategy();
        final TestMonitorConfig testCfg = new TestMonitorConfig();
        StatisticsCollector fixture =
                new StatisticsCollector(testCol, "test-app", "test-inst", testCfg);
        
        // fix up some timers. these timers return a pre-set value when
        // getInclusiveTime is called
        ExecutionTimer A_a1 = createTimer(1000, "A", "a1");
        ExecutionTimer A_a2 = createTimer(800, "A", "a2");
        ExecutionTimer A_a3 = createTimer(100, "A", "a3");
        ExecutionTimer B_b1 = createTimer(300, "B", "b1");
        ExecutionTimer B_b2 = createTimer(200, "B", "b2");
        ExecutionTimer C_c1 = createTimer(200, "C", "c1");
        ExecutionTimer C_c2 = createTimer(100, "C", "c2");
        ExecutionTimer C_c3 = createTimer(100, "C", "c3");
        ExecutionTimer D_d1 = createTimer(5, "D", "d1");
        ExecutionTimer D_d2 = createTimer(20, "D", "d2");
        ExecutionTimer E_e1 = createTimer(5, "E", "e1"); // this and D_d1 should be ignored 'cos our
                                                         // prune time is 10ms
        
        // mimick some executions
        // the pattern is as that shown in the comment at the start of the test
        fixture.recordExecutionBegin(A_a1);
        fixture.recordExecutionBegin(A_a2);
        fixture.recordExecutionBegin(B_b1);
        fixture.recordExecutionBegin(C_c1);
        fixture.recordExecutionEnd(C_c1);
        fixture.recordExecutionEnd(B_b1);
        fixture.recordExecutionBegin(B_b2);
        fixture.recordExecutionBegin(A_a3);
        fixture.recordExecutionEnd(A_a3);
        fixture.recordExecutionEnd(B_b2);
        fixture.recordExecutionBegin(C_c2);
        fixture.recordExecutionEnd(C_c2);
        fixture.recordExecutionEnd(A_a2);
        fixture.recordExecutionBegin(C_c3);
        fixture.recordExecutionBegin(D_d1);
        fixture.recordExecutionEnd(D_d1);
        fixture.recordExecutionBegin(D_d2);
        fixture.recordExecutionEnd(D_d2);
        fixture.recordExecutionBegin(E_e1);
        fixture.recordExecutionEnd(E_e1);
        fixture.recordExecutionEnd(C_c3);
        fixture.recordExecutionEnd(A_a1);
        
        // test if the end of the operation is indentified and some stats is recorded
        List stats = testCol.getStats();
        assertEquals(1, stats.size());
        OperationStatistics opStats = (OperationStatistics) stats.get(0);
        
        // test if the layers are identified correctly
        String[] layers = opStats.getLayers();
        Arrays.sort(layers);
        assertEquals(5, layers.length);
        assertEquals("A", layers[0]);
        assertEquals("A.B", layers[1]);
        assertEquals("A.B.C", layers[2]);
        assertEquals("A.C", layers[3]);
        assertEquals("A.C.D", layers[4]); //A.C.E is notable by its absensce; it was pruned
        
        // test if all the layer times are recorded
        // A_a1 goes into this
        assertEquals(1000, opStats.getTimeInLayer("A"));
        
        // A_a1.A_a2.B_b1 and A_a1.A_a2.B_b2 goes into this
        assertEquals(500, opStats.getTimeInLayer("A.B"));
        
        //A_a1.A_a2.B_b1.C_c1 (200) goes into this
        assertEquals(200, opStats.getTimeInLayer("A.B.C"));
        
        //A_a1.A_a2.C_c2 (100) and A_a1.C_c3 (100) goes into this
        assertEquals(200, opStats.getTimeInLayer("A.C"));
        
        // A_a1.C_c3.D_d2 goes into this
        assertEquals(20, opStats.getTimeInLayer("A.C.D"));
        
        assertEquals(-1, opStats.getTimeInLayer("A.C.E")); // no layer time recorded for A.C.E
        
        Tree tree = opStats.getOperationTree();
        assertNotNull(tree);
        TreeNode root = tree.getRoot();
        assertSame(A_a1, root.getValue());
        
        assertDepth(root, A_a2, 1);
        assertDepth(root, C_c3, 1);
        assertDepth(root, B_b1, 2);
        assertDepth(root, B_b2, 2);
        assertDepth(root, C_c2, 2);
        assertDepth(root, D_d2, 2);
        assertDepth(root, C_c1, 3);
        assertDepth(root, A_a3, 3);
        
        // test if two pruned nodes are in the tree
        assertNull(root.find(D_d1));
        assertNull(root.find(E_e1));
        
        ExecutionTimer[] exec_A = opStats.getExecutions("A");
        assertEquals(2, exec_A.length); // A_a1 and A_a2
        ExecutionTimer[] exec_A_B = opStats.getExecutions("A.B");
        
        // Here is a catch with heirarchical layers!
        // Executions under A.B included B_b1, B_b2 AND A_a3 too!
        assertEquals(3, exec_A_B.length);
        
        ExecutionTimer[] exec_A_B_C = opStats.getExecutions("A.B.C");
        assertEquals(1, exec_A_B_C.length); //C_c1
        
        ExecutionTimer[] exec_A_C = opStats.getExecutions("A.C");
        assertEquals(2, exec_A_C.length); // C_c2, C_c3
        
        ExecutionTimer[] exec_A_C_D = opStats.getExecutions("A.C.D");
        assertEquals(1, exec_A_C_D.length); // only D_d2, no D_d1 which is pruned
        assertNotSame(D_d1, exec_A_C_D[0]); // make sure that it *is* pruned
        
        ExecutionTimer[] exec_A_C_E = opStats.getExecutions("A.C.E");
        // no executions recorded for A.C.E - the only execution in that layer was pruned
        assertEquals(0, exec_A_C_E.length);
    }
    
    // assert if there is a TreeNode with value under the root node, at the specified depth
    private void assertDepth(TreeNode root, Object value, int expectedDepth) {
        TreeNode node = root.find(value);
        assertNotNull(node);
        assertEquals(expectedDepth, node.getDepth());
    }
    
    
    private ExecutionTimer createTimer(final long time, final String layer, final String name) {
        
        class TestExecutionContext implements ExecutionContext {
            private String layer, name;
            public TestExecutionContext(String layer, String name) {
                this.layer = layer; this.name = name;
            }
            public void addChild(net.sf.infrared.base.model.ExecutionContext ctx) { }
            public List getChildren() { return null; }
            public String getLayer() { return layer; }
            public String getName() { return name; }
            public ExecutionContext getParent() { return null; }
            public String toString() {
                return layer + " - " + name;
            }
            public boolean equals(Object o) {
                if (o == null) return false;
                if (o == this) return true;
                if (! (o instanceof TestExecutionContext) ) return false;
                TestExecutionContext other = (TestExecutionContext) o;
                return (other.name.equals(this.name))
                && (other.layer.equals(this.layer));
            }
            public int hashCode() { return name.hashCode() + layer.hashCode(); }
        }
        ExecutionContext mockCtx = new TestExecutionContext(layer, name);
        
        return new ExecutionTimer(mockCtx) {
            public long getInclusiveTime() {
                return time;
            }
        };
    }
    
    class TestMonitorConfig extends MonitorConfigImpl {
        public TestMonitorConfig() {
            super("infrared-agent-root.properties");
        }
        
        public long getPruneThreshold() {
            return 10; // so that we can prune some executions off
        }
        
        public boolean isCallTracingEnabled() {
            return true;
        }
        
        public boolean isMonitoringEnabled() {
            return true;
        }
    }
    
    class TestCollectionStrategy implements CollectionStrategy {
        private List stats = new ArrayList();
        public boolean collect(OperationStatistics stats) {
            this.stats.add(stats); return true;
        }
        public boolean destroy() { return true; }
        public boolean init(MonitorConfig configuration) { return true; }
        public void resume() { }
        public void suspend() { }
        List getStats() { return this.stats; }
    }
    
}




