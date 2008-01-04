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

import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.TestCase;

import org.easymock.MockControl;

public class MonitorFactoryTest extends TestCase {
    protected void setUp() throws Exception {
        MonitorFactory.reset();
    }
    
    protected void tearDown() throws Exception {
        MonitorFactory.reset();
    }

    public void testGettingDefaultFacade() {
        MonitorFacade facade = MonitorFactory.getFacade();
        assertSame(MonitorFactory.getDefaultFacade(), facade);
    }
    
    public void testRegisteringAndUnregisteringOnDifferentThreads() {        
        class TestThread extends Thread {
            private boolean failReg = false;
            private boolean failUnReg = false;
            private ClassLoader cl = new URLClassLoader(new URL[] {}, this.getClass().getClassLoader());
            public void run() {
                MockControl ctrl = MockControl.createNiceControl(MonitorFacade.class);
                MonitorFacade mockFacade = (MonitorFacade) ctrl.getMock();
                
                MonitorFactory.registerFacadeImpl(mockFacade);
                try {
                    assertSame(MonitorFactory.getFacade(), mockFacade);
                } catch (Throwable ex) {
                    failReg = true;
                }
                
                MonitorFactory.unregisterFacadeImpl();
                try {
                    assertSame(MonitorFactory.getDefaultFacade(), MonitorFactory.getFacade());
                } catch (Throwable ex) {
                    failUnReg = true;
                }
            }
            
            public ClassLoader getContextClassLoader() {
                return cl;
            }
            
            public boolean isRegFailed() {
                return failReg;
            }
            
            public boolean isUnRegFailed() {
                return failUnReg;
            }
        };
        
        TestThread t1 = new TestThread(); TestThread t2 = new TestThread();
        t1.start(); t2.start();
        
        try { t1.join(); } catch (Throwable th) { }
        try { t2.join(); } catch (Throwable th) { }
        
        assertFalse(t1.isRegFailed()); assertFalse(t2.isRegFailed());
        assertFalse(t1.isUnRegFailed()); assertFalse(t2.isUnRegFailed());
        assertSame(MonitorFactory.getDefaultFacade(), MonitorFactory.getFacade());
    }
}
