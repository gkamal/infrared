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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.infrared.agent.setup.InfraREDLifeCycleListenerTest;
import net.sf.infrared.agent.setup.InfraREDServletContextListenerTest;
import net.sf.infrared.agent.transport.impl.BufferedAggregatorTest;
import net.sf.infrared.agent.transport.impl.PeriodicFlushPolicyTest;
import net.sf.infrared.agent.transport.impl.SocketWriterTest;
import net.sf.infrared.agent.util.MutableIntegerTest;

public class TestAll extends TestCase {
    public TestAll(String str) {
        super(str);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(ChildTimeTrackerImplTest.class));
        suite.addTest(new TestSuite(LayerTimeTrackerTest.class));
        suite.addTest(new TestSuite(StatisticsCollectorTest.class));
        suite.addTest(new TestSuite(BufferedAggregatorTest.class));
        suite.addTest(new TestSuite(PeriodicFlushPolicyTest.class));
        suite.addTest(new TestSuite(MultipleEntryGuardTest.class));
        suite.addTest(new TestSuite(MonitorConfigImplTest.class));
        suite.addTest(new TestSuite(MonitorFacadeImplTest.class));
        suite.addTest(new TestSuite(MonitorFactoryTest.class));
        suite.addTest(new TestSuite(InfraREDLifeCycleListenerTest.class));
        suite.addTest(new TestSuite(InfraREDServletContextListenerTest.class));
        suite.addTest(new TestSuite(TreeBuilderTest.class));
        suite.addTest(new TestSuite(SocketWriterTest.class));
        suite.addTest(new TestSuite(MutableIntegerTest.class));
        return suite;
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
