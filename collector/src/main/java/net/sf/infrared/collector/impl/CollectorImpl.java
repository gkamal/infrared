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

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import net.sf.infrared.base.model.StatisticsSnapshot;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.collector.Collector;
import net.sf.infrared.collector.CollectorConfig;
import net.sf.infrared.collector.StatisticsRepository;
import net.sf.infrared.collector.impl.persistence.Persister;
import net.sf.infrared.collector.impl.transport.AgentListener;

import org.apache.log4j.Logger;

public class CollectorImpl implements Collector {
    private static final Logger log = LoggingFactory.getLogger(CollectorImpl.class);

    private StatisticsRepository statsRepository;

    private AgentListener agentListener;

    private Persister persister;

    public boolean start(CollectorConfig cfg) {
        statsRepository = new StatisticsRepository();

        try {
            this.agentListener = new AgentListener(cfg, statsRepository);
            agentListener.start();
        } catch (IOException e) {
            log.error("Failed to initialise the AgentListener : IOException", e);
            return false;
        }

        persister = new Persister(cfg, statsRepository);
        persister.start();
        
        log.debug("Started the Collector instance successfully");
        log.debug("The persist interval for the collector is configured at :" 
                + cfg.getPersistInterval() + " milliseconds");
        
        return true;
    }

    public boolean shutdown() {
        if (agentListener != null) {
            agentListener.requestShutdown();
        }

        if (persister != null) {
            persister.shutdown();
        }

        log.debug("Shutdown Collector successfully");
        return true;
    }

    public StatisticsSnapshot fetchStats(Collection appNames, Collection instanceIds) {
        if (appNames == null || instanceIds == null)
            throw new IllegalArgumentException(
                    "The list of application names and instanceIds cannot be null");

        StatisticsSnapshot snapshot = statsRepository.fetchStatsSinceStartup(appNames, instanceIds);
        if (log.isDebugEnabled()) {
            log.debug("Fetched SnapShot (from memory) for applications[" + appNames + 
                    "] and instances[" + instanceIds + "]");
        }
        return snapshot;
    }

    public StatisticsSnapshot fetchStatsFromDB(Collection appNames, Collection instanceIds,
            Date from, Date to) {
        if (appNames == null || instanceIds == null)
            throw new IllegalArgumentException(
                    "The list of application names and instanceIds cannot be null");

        if (from == null)
            throw new IllegalArgumentException("The from date for the fetch cannot be null");

        if (to == null)
            to = new Date();

        StatisticsSnapshot snapshot = statsRepository.fetchStatsFromDB(appNames, instanceIds, from, to);
        if (log.isDebugEnabled()) {
            log.debug("Fetched SnapShot (from DB) for applications[" + appNames + 
                    "] and instances[" + instanceIds + "]");
        }
        return snapshot;
    }
    
    public Set getApplicationNames() {
        return statsRepository.getApplicationNames();        
    }    
    
    
    public Set getInstanceNames(Set applicationNames){
        return statsRepository.getInstanceNames(applicationNames);
    }
    
    public void clearStats() {
        statsRepository.clearStatsSinceStartup();
    }
}
