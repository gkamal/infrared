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

import java.util.Properties;

import junit.framework.TestCase;

public class MonitorConfigImplTest extends TestCase {
    public void testGettingBooleanProperty() {
        Properties testProps = new Properties();
        testProps.setProperty("test1", "true");
        testProps.setProperty("test2", "foo");
        testProps.setProperty("test3", "false");
        MonitorConfigImpl fixture = new MonitorConfigImpl(testProps);
        
        // existing boolean property - should get the correct one, not the default
        assertTrue(fixture.getProperty("test1", false));
        assertTrue(fixture.getProperty("test1", true));
        
        // existing non-boolean property - should get default
        assertFalse(fixture.getProperty("test2", false));
        assertTrue(fixture.getProperty("test2", true));
        
        // non-existing property - should get default
        assertFalse(fixture.getProperty("test4", false));
        assertTrue(fixture.getProperty("test4", true));
        
        // existing boolean property - should get the correct one, not the default
        assertFalse(fixture.getProperty("test3", false));
        assertFalse(fixture.getProperty("test3", true));
    }
    
    public void testGettingIntegerProperty() {
        Properties testProps = new Properties();
        testProps.setProperty("test1", "1");
        testProps.setProperty("test2", "foo");
        MonitorConfigImpl fixture = new MonitorConfigImpl(testProps);
        
        // existing integer property - should get the correct one, not the default
        assertEquals(1, fixture.getProperty("test1", 4));
        
        
        // existing non-ineteger property - should get default
        assertEquals(-1, fixture.getProperty("test2", -1));
        
        // non-existing property - should get default
        assertEquals(5, fixture.getProperty("test3", 5));
    }
    
    public void testGettingLongProperty() {
        Properties testProps = new Properties();
        testProps.setProperty("test1", "1");
        testProps.setProperty("test2", "foo");
        MonitorConfigImpl fixture = new MonitorConfigImpl(testProps);
        
        // existing long property - should get the correct one, not the default
        assertEquals(1L, fixture.getProperty("test1", 4L));
        
        
        // existing non-long property - should get default
        assertEquals(-1L, fixture.getProperty("test2", -1L));
        
        // non-existing property - should get default
        assertEquals(5L, fixture.getProperty("test3", 5L));
    }
    
    public void testGettingStringProperty() {
        Properties testProps = new Properties();
        testProps.setProperty("test1", "foo");
        MonitorConfigImpl fixture = new MonitorConfigImpl(testProps);
        
        // existing string property - should get the correct one, not the default
        assertEquals("foo", fixture.getProperty("test1", "bar"));
        
        // non-existing property - should get default
        assertEquals("bar", fixture.getProperty("test2", "bar"));
    }
}
