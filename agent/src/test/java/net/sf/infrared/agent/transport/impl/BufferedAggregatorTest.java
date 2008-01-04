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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.agent.transport.impl;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import net.sf.infrared.agent.transport.Forwarder;
import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.OperationStatistics;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;

/**
 */
public class BufferedAggregatorTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAggregateAndFlush() {
        ApplicationStatistics testAppStatistics =
                new ApplicationStatistics("testApplication", "testHost");

        MockControl forwarderControl = MockControl.createStrictControl(Forwarder.class);
        Forwarder mockForwarder = (Forwarder) forwarderControl.getMock();
        mockForwarder.forward(testAppStatistics);
        forwarderControl.setMatcher(new ArgumentsMatcher() {
            public boolean matches(Object[] expected, Object[] actual) {
                assertEquals(expected.length, actual.length);
                assertTrue(expected.length == 1);
                assertTrue(actual.length == 1);
                assertTrue("Instance of Statistics", expected[0] instanceof ApplicationStatistics);
                assertTrue("Instance of Statistics", actual[0] instanceof ApplicationStatistics);

                assertTrue("Actual & expected have different application names",
                        ((ApplicationStatistics) actual[0]).getApplicationName().equals(
                                ((ApplicationStatistics) expected[0]).getApplicationName())

                );

                assertTrue("Actual & expected have different instance names",
                        ((ApplicationStatistics) actual[0]).getInstanceId().equals(
                                ((ApplicationStatistics) expected[0]).getInstanceId()));

                return true;
            }

            public String toString(Object[] objects) {
                return null;
            }

        });
        forwarderControl.replay();

        BufferedAggregator aggregator = new BufferedAggregator();
        aggregator.setForwarder(mockForwarder);

        OperationStatistics testOpStatistics =
                new OperationStatistics("testApplication", "testHost");
        aggregator.aggregate(testOpStatistics);

        aggregator.flush();
        forwarderControl.verify();
    }

    public void testFlushWihoutAggregate() {
        MockControl forwarderControl = MockControl.createStrictControl(Forwarder.class);
        Forwarder mockForwarder = (Forwarder) forwarderControl.getMock();
        forwarderControl.replay();

        BufferedAggregator aggregator = new BufferedAggregator();
        aggregator.setForwarder(mockForwarder);
        aggregator.flush();
        forwarderControl.verify();
    }

    public void testNullForwarder() {
        BufferedAggregator aggregator = new BufferedAggregator();
        aggregator.flush(); // no exceptions should be thrown
    }

    public void testNoAggregateBetweenFlushes() {
        ApplicationStatistics testAppStatistics =
                new ApplicationStatistics("testApplication", "testHost");

        MockControl forwarderControl = MockControl.createStrictControl(Forwarder.class);
        Forwarder mockForwarder = (Forwarder) forwarderControl.getMock();
        mockForwarder.forward(testAppStatistics);
        forwarderControl.setMatcher(new ArgumentsMatcher() {
            public boolean matches(Object[] expected, Object[] actual) {
                assertEquals(expected.length, actual.length);
                assertTrue(expected.length == 1);
                assertTrue(actual.length == 1);
                assertTrue("Instance of Statistics", expected[0] instanceof ApplicationStatistics);
                assertTrue("Instance of Statistics", actual[0] instanceof ApplicationStatistics);

                assertTrue("Actual & expected have different application names",
                        ((ApplicationStatistics) actual[0]).getApplicationName().equals(
                                ((ApplicationStatistics) expected[0]).getApplicationName())

                );

                assertTrue("Actual & expected have different instance names",
                        ((ApplicationStatistics) actual[0]).getInstanceId().equals(
                                ((ApplicationStatistics) expected[0]).getInstanceId()));

                return true;
            }

            public String toString(Object[] objects) {
                return null;
            }

        });
        forwarderControl.replay();

        BufferedAggregator aggregator = new BufferedAggregator();
        aggregator.setForwarder(mockForwarder);

        OperationStatistics testOpStatistics =
                new OperationStatistics("testApplication", "testHost");
        aggregator.aggregate(testOpStatistics);

        aggregator.flush();
        aggregator.flush();
        forwarderControl.verify();
    }

    public static void main(String[] args) {
        TestRunner.run(BufferedAggregatorTest.class);
    }
}
