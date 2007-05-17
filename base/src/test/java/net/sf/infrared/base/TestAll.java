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
package net.sf.infrared.base;

import net.sf.infrared.base.model.AggregateExecutionTimeTest;
import net.sf.infrared.base.model.AggregateOperationTreeTest;
import net.sf.infrared.base.model.ApplicationStatisticsTest;
import net.sf.infrared.base.model.ExecutionTimeTest;
import net.sf.infrared.base.util.TreeNodeTest;
import net.sf.infrared.base.util.TreeTest;
import net.sf.infrared.base.util.LoggingFactoryTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase {
    public TestAll(String str) {
        super(str);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(AggregateExecutionTimeTest.class));
        suite.addTest(new TestSuite(AggregateOperationTreeTest.class));
        suite.addTest(new TestSuite(ExecutionTimeTest.class));
        suite.addTest(new TestSuite(TreeNodeTest.class));
        suite.addTest(new TestSuite(TreeTest.class));
        suite.addTest(new TestSuite(ApplicationStatisticsTest.class));
        suite.addTest(new TestSuite(LoggingFactoryTest.class));

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
