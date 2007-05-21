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
 * Contributor(s):   kamal.govindraj;
 *
 */
package net.sf.infrared.agent.transport.impl;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PeriodicFlushPolicyTest extends TestCase
{
    private PeriodicFlushPolicy testPolicy;

    public PeriodicFlushPolicyTest(String str)
    {
        super(str);
    }

    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();

        suite.addTest(new TestSuite(PeriodicFlushPolicyTest.class));
        return suite;
    }

    public void setUp()
    {
        testPolicy = new PeriodicFlushPolicy();
        testPolicy.setFrequency(2);
    }

    public void tearDown()
    {
        testPolicy.shutDown();
        testPolicy = null;
    }

    public void testInitialState()
    {
        assertFalse("A newly created PeriodicFlushPolicy should be inactive", testPolicy.isActive());
    }
    
    public void testActivatingANewFlushPolicy()
    {
        assertTrue(testPolicy.activate());
        assertTrue(testPolicy.isActive());
    }

    public void testActivatingAnActiveFlushPolicy()
    {
        testPolicy.activate();
        assertFalse(testPolicy.activate());
        assertTrue(testPolicy.isActive());
    }

    public void testShuttingDownANewFlushPolicy()
    {
        assertFalse(testPolicy.shutDown());
        assertFalse(testPolicy.isActive());
    }

    public void testShuttingDownAnActiveFlushPolicy()
    {
        testPolicy.activate();
        assertTrue(testPolicy.shutDown());
        assertFalse(testPolicy.isActive());
    }

    public void testSettingANegativeFrequencyValue()
    {
        testPolicy.setFrequency(-1L);
        assertTrue(testPolicy.getFrequency() != -1);
    }

    public void testSettingAPositiveFrequencyValue()
    {
        testPolicy.setFrequency(1L);
        assertTrue(testPolicy.getFrequency() == 1);
    }

    public void testSettingZeroFrequencyValue()
    {
        testPolicy.setFrequency(0L);
        assertTrue(testPolicy.getFrequency() != 0);
    }
    
    public static void main(String[] args) throws Exception
    {
        junit.textui.TestRunner.run(new TestSuite(PeriodicFlushPolicyTest.class));
    }
}
