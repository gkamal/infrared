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
package net.sf.infrared.agent.setup;

import junit.framework.TestCase;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.MultipleEntryGuard;

public class InfraREDLifeCycleListenerTest extends TestCase {
    protected void setUp() throws Exception {
        MonitorFactory.reset();
    }
    
    protected void tearDown() throws Exception {
        MonitorFactory.reset();
    }
    
    public void testInitializingAndDestroying() {
        InfraREDLifeCycleListener fixture = new InfraREDLifeCycleListener();
        
        assertSame(MonitorFactory.getFacade(), MonitorFactory.getDefaultFacade());
        
        fixture.initialized("test-app", "test-instance", MonitorFactory.DEFAULT_CONFIG_LOCATION);
        assertNotSame(MonitorFactory.getFacade(), MonitorFactory.getDefaultFacade());
        assertSame(MonitorFactory.getFacade().getClass(), MultipleEntryGuard.class);
        
        fixture.destroyed("test-app");
        assertSame(MonitorFactory.getFacade(), MonitorFactory.getDefaultFacade());
    }
}
