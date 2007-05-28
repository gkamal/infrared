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
 * Contributor(s):   subin.p;
 *
 */
package net.sf.infrared.collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.StatisticsSnapshot;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.collector.impl.persistence.SpringContext;

public class StatisticsRepository {
    // map of names -> ApplicationStatistics, collected since the collector started
    // name = applicationName#instanceId
    private Map mapOfStatsSinceStartup = new HashMap();

    // map of names -> ApplicationStatistics, collected since the last persist
    // name = applicationName#instanceId
    private Map mapOfStatsSinceLastPersist = new HashMap();

    private static final String SEPERATOR = "#";
    
    private SpringContext springContext = new SpringContext();
    
    private static final Logger log = LoggingFactory.getLogger(StatisticsRepository.class);

    public void addStatistics(ApplicationStatistics stats) {
        addStatsToStatsMap(stats, mapOfStatsSinceStartup);
        addStatsToStatsMap(stats, mapOfStatsSinceLastPersist);
    }

    public List getStatisticsToPersist() {
        Map old = mapOfStatsSinceLastPersist;
        
        List statsList = new ArrayList();
        for (Iterator i = old.values().iterator(); i.hasNext();) {
            ApplicationStatistics stats = (ApplicationStatistics) i.next();
            statsList.add(stats);
        }
        return statsList;
    }

    public StatisticsSnapshot fetchStatsSinceStartup(Collection appNames, 
            Collection instanceIds) {
        String appName = null;
        String instanceName = null;
        StringTokenizer tokenizer = null;

        StatisticsSnapshot stats = new StatisticsSnapshot();
        Set keySet = mapOfStatsSinceStartup.keySet();
        for (Iterator iter = keySet.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            tokenizer = new StringTokenizer(element, SEPERATOR);
            appName = tokenizer.nextToken();
            instanceName = tokenizer.nextToken();

            if (appNames.contains(appName) && instanceIds.contains(instanceName)) {
                stats.merge((ApplicationStatistics) mapOfStatsSinceStartup.get(element));
            }
        }
        return stats;
    }

    public StatisticsSnapshot fetchStatsFromDB(Collection appNames, Collection instanceIds,
            Date from, Date to) {

        return springContext.getDao().fetchStatistics(appNames, instanceIds, from, to);
    }
    
    public Set getApplicationNames(){
        Set keys = mapOfStatsSinceStartup.keySet();
        Set applicationNames = new HashSet();
        
        for (Iterator iter = keys.iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            String [] tokens = key.split(SEPERATOR);
            applicationNames.add(tokens[0]);                       
        }        
        return applicationNames;
    }
    

    public Set getInstanceNames(Set applicationNames){
        Set keys = mapOfStatsSinceStartup.keySet();
        Set instanceNames = new HashSet();
        
        for (Iterator iter = keys.iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            String [] tokens = key.split(SEPERATOR);
            if(applicationNames.contains(tokens[0])){
                instanceNames.add(tokens[1]);
            }                                  
        }        
        return instanceNames;        
    }

    // @TODO Need to decide if we need to persist the data that needs to be cleared during 
    // this call in this method or need to continue with the timer persistence. 
    public void clearStatsSinceStartup() {
        if(mapOfStatsSinceStartup != null) {
            mapOfStatsSinceStartup.clear();
        }
    }


    public void clearStatsSinceLastPersist() {
        if(mapOfStatsSinceLastPersist != null) {
            mapOfStatsSinceLastPersist.clear();            
        }
        log.debug("Cleared the statistics that were persisted.");
    }
    
    
    private void addStatsToStatsMap(ApplicationStatistics stats, Map map) {
        String applicationName = stats.getApplicationName();
        String instanceId = stats.getInstanceId();

        String statsKey = constructStatsKey(applicationName, instanceId);

        synchronized (map) {
            ApplicationStatistics appStats = (ApplicationStatistics) map.get(statsKey);

            if (appStats == null) {
                map.put(statsKey, stats);
            } else {
                appStats.merge(stats);
            }
        }
    }

    private String constructStatsKey(String applicationName, String instanceName) {
        if (applicationName == null)
            throw new IllegalArgumentException("The application name cannot be null");
        else if (instanceName == null)
            throw new IllegalArgumentException("the instance name cannot be null");

        return applicationName + SEPERATOR + instanceName;
    }
}