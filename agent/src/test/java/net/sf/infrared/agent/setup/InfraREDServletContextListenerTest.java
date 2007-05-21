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

import javax.servlet.ServletContext;

import net.sf.infrared.agent.MonitorConfigImpl;

import org.easymock.MockControl;

import junit.framework.TestCase;

public class InfraREDServletContextListenerTest extends TestCase {
    boolean pass = false;
    
    public void testIfAppNameAndConfigProviderAreNotSet() {
        MockControl ctrl = MockControl.createControl(ServletContext.class);
        ServletContext mockCtx = (ServletContext) ctrl.getMock();
        mockCtx.getAttribute(InfraREDServletContextListener.KEY_CONFIGURATIONPROVIDER);
        ctrl.setReturnValue(null);
        mockCtx.getServletContextName();
        ctrl.setReturnValue(null);
        ctrl.replay();
        
        InfraREDServletContextListener fixture = new InfraREDServletContextListener() {
            InfraREDLifeCycleListener getLifeCycleListener() {
                return new InfraREDLifeCycleListener() {

                    public void initialized(String name, String instance, String cfg) {
                        pass = true;
                        assertEquals("unknown", name);
                        assertEquals(MonitorConfigImpl.DEFAULT_CONFIG_LOCATION, cfg);
                    }
                };
            }
        };
        
        fixture.initialized(mockCtx);
        
        ctrl.verify();
        if (!pass) {
            fail();
        }
    }
}
