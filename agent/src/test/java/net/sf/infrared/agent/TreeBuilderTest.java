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

import junit.framework.TestCase;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

import org.easymock.MockControl;

public class TreeBuilderTest extends TestCase {
    /*
     * et1
     *  +----- et2
     *          +----- et3 (prunable)
     *  +----- et4
     *  +----- et5 (prunable)
     */
    public void testIfTreeIsBuildingCorrectlyAndPruningIsHappening() {
        TreeBuilder fixture = new TreeBuilder() {
            public long getPruneBelowTime() {
                return 10;
            }
        };
        
        ExecutionTimer t1 = createTimer(100);
        ExecutionTimer t2 = createTimer(60);
        ExecutionTimer t3 = createTimer(5);
        ExecutionTimer t4 = createTimer(20);
        ExecutionTimer t5 = createTimer(5);
        
        fixture.begin(t1);
            fixture.begin(t2);
                fixture.begin(t3);
                fixture.end(t3);
            fixture.end(t2);
            
            fixture.begin(t4);
            fixture.begin(t4);
            
            fixture.begin(t5);
            fixture.end(t5);
        fixture.end(t1);
        
        Tree tree = fixture.reset();
        
        TreeNode et1Node = tree.getRoot();
        
        assertSame("et1 should have been root", t1, et1Node.getValue());
        assertEquals("et1 should have two children - et2 and et4", 2, et1Node.getChildren().size());
        
        TreeNode et2Node = (TreeNode) et1Node.getChildren().get(0);
        TreeNode et4Node = (TreeNode) et1Node.getChildren().get(1);
        
        assertEquals("et5 should have been pruned", 2, et1Node.getChildren().size()); 
        assertSame("et1 should have been a child of et1", t2, et2Node.getValue());
        assertSame("et4 should have been a child of et1", t4, et4Node.getValue());
        assertEquals("et3 should have been pruned", 0, et2Node.getChildren().size());
    }

    public void testFaultyCondition() {
        // @TODO handle faulty trees gracefully
    }
    
    private ExecutionTimer createTimer(final long time) {
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        final ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        
        return new ExecutionTimer(mockCtx) {
            public long getInclusiveTime() {
                return time;
            }
        };
    }
}
