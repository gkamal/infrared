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
 * Original Author:  subin.p (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.collector.impl;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;
import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.collector.Collector;

public class CollectorImplTest  extends TestCase {

    public void testFetchStatsFromDBForNullDates() {
    
        Collector collector = new CollectorImpl();
        ArrayList temp = new ArrayList();
        try {
            collector.fetchStatsFromDB(temp, temp, null, null);
            fail();
        } catch (IllegalArgumentException expected) {
            System.out.println(expected.toString());
        } catch (Throwable unexpected) {
            fail();
        }           
    }
    
    // test for empty collection list. Need to change the implementation.
/*    public void testFetchStatsFromDBForNullToDate() {

        Collector collector = new CollectorImpl();
        ArrayList temp = new ArrayList();
        
        ApplicationStatistics stats = collector.fetchStatsFromDB(temp, temp, new Date(), null);
        System.out.println("Empty ??" + stats);
        assertEquals(stats, null);
    } */
}
