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
import net.sf.infrared.agent.transport.CollectionStrategy;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;

import org.easymock.MockControl;

public class StatisticsCollectorTest extends TestCase {
    
    public void testIfExclusiveTimeIsSetCorrectly() {
        StatisticsCollector fixture = new StatisticsCollector() {
            long getPruneBelowTime() {
                return 10;
            }
            
            boolean isCallTracingEnabled() {
                return false;
            }
            
            public CollectionStrategy getCollectionStrategy() {
                MockControl ctrl = MockControl.createNiceControl(CollectionStrategy.class);
                CollectionStrategy mockStrategy = (CollectionStrategy) ctrl.getMock();
                return mockStrategy;
            }

			public MonitorConfig getConfiguration() {
				return new MonitorConfigImpl();
			}
        };

        ExecutionTimer et1 = createTimer(100);
        ExecutionTimer et2 = createTimer(60);
        ExecutionTimer et3 = createTimer(5);
        ExecutionTimer et4 = createTimer(2);
        ExecutionTimer et5 = createTimer(20);
        ExecutionTimer et6 = createTimer(25);

        fixture.recordExecutionBegin(et1);
            fixture.recordExecutionBegin(et2);
                fixture.recordExecutionBegin(et3);
                    fixture.recordExecutionBegin(et4);
                    fixture.recordExecutionEnd(et4);
                fixture.recordExecutionEnd(et3);
                
                fixture.recordExecutionBegin(et5);
                fixture.recordExecutionEnd(et5);
            fixture.recordExecutionEnd(et2);

            fixture.recordExecutionBegin(et6);
            fixture.recordExecutionEnd(et6);
        fixture.recordExecutionEnd(et1);
        
        assertEquals("et4 has no child methods, hence exclusive time should have been " +
                "same as inclusive time 2", 2, et4.getExclusiveTime());
        // @TODO this might be a bug!!
        assertEquals("et3 will be pruned, hence inclusive time should have been " +
                "same as exclusive time 5", 5, et3.getExclusiveTime());
        
        assertEquals("et2's one child(et3) will be pruned but the other(et5) wont be, " +
                "hence inclusive time should have been 60-20=40", 40, et2.getExclusiveTime());
        
        assertEquals("et6 has no child methods, hence exclusive time is " +
                "same as inclusive time 25", 25, et6.getExclusiveTime());
        
        assertEquals("et1's child methods time should be deducted (100-60-25=15)", 
                15, et1.getExclusiveTime());
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(StatisticsCollectorTest.class);
    }

    ExecutionTimer createTimer(final long time) {
        MockControl ctrl = MockControl.createControl(ExecutionContext.class);
        final ExecutionContext mockCtx = (ExecutionContext) ctrl.getMock();
        mockCtx.getLayer();
        ctrl.setDefaultReturnValue("TestLayer");
        ctrl.replay();
        
        return new ExecutionTimer(mockCtx) {
            public long getInclusiveTime() {
                return time;
            }
        };
    }
}
