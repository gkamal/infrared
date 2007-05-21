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

public class ChildTimeTrackerImplTest extends TestCase {
    // legend = method(inclusiveTime, exclusiveTime)
   
    /* 
     * one (40, 30)
     *  +----- two (10, 5)
     *          +------ three (2, 2)
     *          +------ four (3, 3)
     */
    public void testChildExecutionTracking() {
        ChildTimeTracker fixture = new ChildTimeTrackerImpl();
        
        fixture.begin();
        
            fixture.begin();
            
                fixture.begin();
                fixture.recordChildExecutionTime(2);
                fixture.end();
                
                fixture.begin();
                fixture.recordChildExecutionTime(3);
                fixture.end();
                
            fixture.recordChildExecutionTime(10);
            assertEquals("", 5, fixture.getChildExecutionTime());
            fixture.end();
            
        fixture.recordChildExecutionTime(40);
        assertEquals("", 10, fixture.getChildExecutionTime());
        fixture.end();
    }
    
    /*
     * one (100, 1)
     *  +--- two (99, 1)
     *        +---- three (98, 1) 
     *               +----- ...
     *                        +---- ninety-eight (3, 1)
     *                               +------------- ninety-nine (2, 1)
     *                                               +------------ hundred (1, 1)
     */
    public void testChildExecutionTrackingDeep() {
        ChildTimeTracker fixture = new ChildTimeTrackerImpl();
        
        for (int i = 1; i <= 100; i++) {
            fixture.begin();
        }
        
        for (int i = 1; i <= 100; i++) {
            fixture.recordChildExecutionTime(i);
            assertEquals("", i - 1, fixture.getChildExecutionTime());
            fixture.end();
        }
    }
}
