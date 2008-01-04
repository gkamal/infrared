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

import org.easymock.MockControl;

public class MultipleEntryGuardTest extends TestCase {
    boolean fail = false;

    public void xtestIfMultipleEntriesAreBlockedOnBegin() {
        MonitorFacade testFacade = new TestMonitorFacade() {
            int i = 0;
            public StatisticsCollector recordExecutionBegin(ExecutionTimer timer) {
                if (++i > 1) {
                    MultipleEntryGuardTest.this.fail = true;
                }
                return MonitorFactory.getFacade().recordExecutionBegin(timer);
            }
        };
        MultipleEntryGuard fixture = new MultipleEntryGuard(testFacade);
        MonitorFactory.registerFacadeImpl(fixture);
        
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionTimer timer = new ExecutionTimer((ExecutionContext) ctrl.getMock());
        MonitorFactory.getFacade().recordExecutionBegin(timer);
        if (fail) {
            fail();
        }
    }
    
    public void testIfExceptionsAreSuppressedOnBegin() {
        MonitorFacade testFacade = new TestMonitorFacade() {
            public StatisticsCollector recordExecutionBegin(ExecutionTimer timer) {
                throw new RuntimeException("test");
            }
        };
        MultipleEntryGuard fixture = new MultipleEntryGuard(testFacade);
        MonitorFactory.registerFacadeImpl(fixture);
        
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionTimer timer = new ExecutionTimer((ExecutionContext) ctrl.getMock());
        try {
            MonitorFactory.getFacade().recordExecutionBegin(timer);
        } catch (RuntimeException ex) {
            fail();
        }
    }
    
    public void xtestIfMultipleEntriesAreBlockedOnEnd() {
        MonitorFacade testFacade = new TestMonitorFacade() {
            int i = 0;
            public void recordExecutionEnd(ExecutionTimer timer) {
                if (++i > 1) {
                    MultipleEntryGuardTest.this.fail = true;
                }
                MonitorFactory.getFacade().recordExecutionEnd(timer);
            }
        };
        MultipleEntryGuard fixture = new MultipleEntryGuard(testFacade);
        MonitorFactory.registerFacadeImpl(fixture);
        
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionTimer timer = new ExecutionTimer((ExecutionContext) ctrl.getMock());
        MonitorFactory.getFacade().recordExecutionEnd(timer);
        if (fail) {
            fail();
        }
    }
    
    public void testIfExceptionsAreSuppressedOnEnd() {
        MonitorFacade testFacade = new TestMonitorFacade() {
            public void recordExecutionEnd(ExecutionTimer timer) {
                throw new RuntimeException("test");
            }
        };
        MultipleEntryGuard fixture = new MultipleEntryGuard(testFacade);
        MonitorFactory.registerFacadeImpl(fixture);
        
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionTimer timer = new ExecutionTimer((ExecutionContext) ctrl.getMock());
        try {
            MonitorFactory.getFacade().recordExecutionEnd(timer);
        } catch (RuntimeException ex) {
            fail();
        }
    }
    
    public void testIfMonitoringIsDisableAfterManyErrors() {
        MonitorFacade testFacade = new TestMonitorFacade() {
            public void recordExecutionEnd(ExecutionTimer timer) {
                throw new RuntimeException("test");
            }
        };
        MultipleEntryGuard fixture = new MultipleEntryGuard(testFacade);
        MonitorFactory.registerFacadeImpl(fixture);
        
        MockControl ctrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionTimer timer = new ExecutionTimer((ExecutionContext) ctrl.getMock());
        
        assertTrue(MonitorFactory.getFacade().isMonitoringEnabled()); // initially monitoring was on
        for (int i = 0; i < 9; i++) {
            MonitorFactory.getFacade().recordExecutionEnd(timer);
            assertTrue(MonitorFactory.getFacade().isMonitoringEnabled()); //it is on thru 9 errors
        }
        MonitorFactory.getFacade().recordExecutionEnd(timer);
        assertFalse(MonitorFactory.getFacade().isMonitoringEnabled()); // on the 10-th its turned off
    }
    
    class TestMonitorFacade implements MonitorFacade {
        private MonitorConfig cfg = new TestMonitorConfig();
        
        public String getApplicationName() {
            return "test app";
        }
        
        public MonitorConfig getConfiguration() {
            return cfg;
        }
        
        public String getInstanceId() {
            return "test instance";
        }
        
        public boolean isMonitoringEnabled() {
            return cfg.isMonitoringEnabled();
        }

        public void recordExecutionEnd(ExecutionTimer timer) {
        }
        
        public StatisticsCollector recordExecutionBegin(ExecutionTimer timer) {
            return null;
        }
        
        public void recordExecutionEnd(ExecutionTimer timer, StatisticsCollector collector) {
        }
        
        public void destroy() {
        }
    }
    
    class TestMonitorConfig extends MonitorConfigImpl {
        private boolean monitoring = true;
        
        public TestMonitorConfig() {
            super("infrared-agent-root.properties");
        }
        
        public boolean isMonitoringEnabled() {
            return monitoring;
        }
        
        public void enableMonitoring(boolean enable) {
            this.monitoring = enable;
        }
    }
}


