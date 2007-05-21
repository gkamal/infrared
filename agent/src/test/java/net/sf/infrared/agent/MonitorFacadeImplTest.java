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

import net.sf.infrared.agent.transport.impl.DoNothingCollectionStrategy;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;

import org.easymock.MockControl;

import junit.framework.TestCase;

public class MonitorFacadeImplTest extends TestCase {
    boolean fail = true;
    
    MockControl cfgCtrl;
    
    MonitorConfig mockCfg;
    
    protected void setUp() throws Exception {
        cfgCtrl = MockControl.createControl(MonitorConfig.class);
        mockCfg = (MonitorConfig) cfgCtrl.getMock();
        mockCfg.getCollectionStrategy(); cfgCtrl.setReturnValue("blah blah"); 
    }

    public void testIfErrorsInFindingCollectionStrategyIsHandledGracefully() {
        cfgCtrl.replay();
        
        MonitorFacadeImpl fixture = new MonitorFacadeImpl("test-app", "test-instance", mockCfg);
        assertEquals(DoNothingCollectionStrategy.class, fixture.getCollectionStrategy().getClass());
    }
    
 /*   public void testRecordBeginWhenMonitoringIsDisabled() { 
        // disable monitoring
        mockCfg.isMonitoringEnabled(); cfgCtrl.setReturnValue(false);
        cfgCtrl.replay();
        
        MonitorFacadeImpl fixture = new MonitorFacadeImpl("test-app", "test-instance", mockCfg) {
            public StatisticsCollector getStatisticsCollectorOfThisThread() {
                return new StatisticsCollector() {
                    public void recordExecutionBegin(ExecutionTimer timer) {
                        fail();
                    }
                };
            }
        };
        
        fixture.recordExecutionBegin(null);
        cfgCtrl.verify();
    }    
    
    public void testRecordBeginWhenMonitoringIsEnabled() {    
        // enable monitoring
        mockCfg.isMonitoringEnabled(); cfgCtrl.setReturnValue(true);
        mockCfg.isMonitoringEnabledForCurrentThread(); cfgCtrl.setReturnValue(true);
        cfgCtrl.replay();
        
        MockControl ctxCtrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctxCtrl.getMock();
        final ExecutionTimer testTimer = new ExecutionTimer(mockCtx);
        
        MonitorFacadeImpl fixture = new MonitorFacadeImpl("test-app", "test-instance", mockCfg) {
            public StatisticsCollector getStatisticsCollectorOfThisThread() {
                return new StatisticsCollector() {
                    public void recordExecutionBegin(ExecutionTimer timer) {
                        fail = false;
                        assertSame(testTimer, timer);
                    }
                };
            }
        };
        
        fixture.recordExecutionBegin(testTimer);
        if (fail) {
            fail();
        }
        cfgCtrl.verify();
    }
    
    public void testRecordEndWhenMonitoringIsDisabled() {        
        // disable monitoring
        mockCfg.isMonitoringEnabled(); cfgCtrl.setReturnValue(false);
        cfgCtrl.replay();
        
        MonitorFacadeImpl fixture = new MonitorFacadeImpl("test-app", "test-instance", mockCfg) {
            public StatisticsCollector getStatisticsCollectorOfThisThread() {
                return new StatisticsCollector() {
                    public void recordExecutionEnd(ExecutionTimer timer) {
                        fail();
                    }
                };
            }
        };
        
        fixture.recordExecutionEnd(null);
        cfgCtrl.verify();
    }     
    
    public void testRecordEndWhenMonitoringIsEnabled() {                
        // enable monitoring
        mockCfg.isMonitoringEnabled(); cfgCtrl.setReturnValue(true);
        mockCfg.isMonitoringEnabledForCurrentThread(); cfgCtrl.setReturnValue(true);
        cfgCtrl.replay();
        
        MockControl ctxCtrl = MockControl.createNiceControl(ExecutionContext.class);
        ExecutionContext mockCtx = (ExecutionContext) ctxCtrl.getMock();
        final ExecutionTimer testTimer = new ExecutionTimer(mockCtx);
        
        MonitorFacadeImpl fixture = new MonitorFacadeImpl("test-app", "test-instance", mockCfg) {
            public StatisticsCollector getStatisticsCollectorOfThisThread() {
                return new StatisticsCollector() {
                    public void recordExecutionEnd(ExecutionTimer timer) {
                        fail = false;
                        assertSame(testTimer, timer);
                    }
                };
            }
        };
        
        fixture.recordExecutionEnd(testTimer);
        if (fail) {
            fail();
        }
        cfgCtrl.verify();
    } */
}