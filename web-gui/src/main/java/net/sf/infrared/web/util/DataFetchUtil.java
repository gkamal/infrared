/*
 *
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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.web.util;

import java.util.Date;
import java.util.Set;

import net.sf.infrared.base.model.StatisticsSnapshot;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.collector.Collector;

import org.apache.log4j.Logger;

public class DataFetchUtil {
        
    static Collector collector;
    static final String SEPERATOR = "#";
    private static final Logger logger = LoggingFactory.getLogger(DataFetchUtil.class);

        
    public static PerformanceDataSnapshot getPerfData(Set appNameSet, Set instNameSet){
        //[BINIL] ApplicationStatistics mergedStats = getCollector().fetchStats(appNameSet, instNameSet);
        StatisticsSnapshot mergedStats = getCollector().fetchStats(appNameSet, instNameSet);
        PerformanceDataSnapshot snapShot = new PerformanceDataSnapshot();
        snapShot.setApplicationNames(appNameSet);
        snapShot.setInstanceNames(instNameSet);
        snapShot.setStats(mergedStats);
        
        return snapShot;
    }

    public static PerformanceDataSnapshot reset(){
    	getCollector().clearStats();
        
        PerformanceDataSnapshot snapShot = new PerformanceDataSnapshot();
        return snapShot;
    }
    
    public static PerformanceDataSnapshot getDataFromDB(Set applications, Set instances,
    											Date fromDate, Date toDate){
    	StatisticsSnapshot stats = getCollector().fetchStatsFromDB(applications, instances,
    											fromDate, toDate);
        PerformanceDataSnapshot snapShot = new PerformanceDataSnapshot();
        snapShot.setApplicationNames(applications);
        snapShot.setInstanceNames(instances);
        snapShot.setStats(stats);
        
        return snapShot;
    	
    }

    public static Set getApplicationNames(){
        return getCollector().getApplicationNames();
    }
    
    public static Set getInstanceNames(Set applicationNames){
        return getCollector().getInstanceNames(applicationNames);
    }
    
    public static void setCollector(Collector collectorImpl){
        collector = collectorImpl;
    }
    
    public static Collector getCollector(){
        if(collector == null){
            logger.error("Collector has not been initialized.");
            throw new RuntimeException("Collector has not been initialized");
        }
        return collector;
    }
    

}
