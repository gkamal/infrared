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
 * Contributor(s):   kamal.govindraj
 *
 */

package net.sf.infrared.agent;

import net.sf.infrared.agent.transport.CollectionStrategy;
import net.sf.infrared.agent.transport.impl.DoNothingCollectionStrategy;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * 
 * @author binil.thomas
 * @author kamal.govindraj
 */
public class MonitorFacadeImpl implements MonitorFacade {
    public static final String KEY_PRINT_FACADE_CREATION = "infrared.print.applications";
    
    private static final Logger log = LoggingFactory.getLogger(MonitorFacadeImpl.class);

    private ThreadLocal statisticsCollector = new ThreadLocal() {
        protected synchronized Object initialValue() {
            if (log.isDebugEnabled()) {
                log.debug("Initializing statistics collector for thread " + Thread.currentThread()
                        + "\n" + "\t collectionStratergy = " + collectionStrategy + "\n"
                        + "\t applicationName = " + applicationName + "\n" + "\t instanceId = "
                        + instanceId);
            }
            return new StatisticsCollector(collectionStrategy, 
                    applicationName, instanceId, configuration);
        }
    };

    private CollectionStrategy collectionStrategy = null;

    private String applicationName;

    private String instanceId;

    private MonitorConfig configuration;
    
    public MonitorFacadeImpl(String applicationName, 
            String instanceId, MonitorConfig config, boolean print) {
        
        this(applicationName, instanceId, config);
        if (print && Boolean.getBoolean(KEY_PRINT_FACADE_CREATION)) {
            System.out.println("[InfraRED] Created MonitorFacade for " + applicationName 
                + ", instance " + instanceId + ", with config " + config);
        }
    }

    public MonitorFacadeImpl(String applicationName, String instanceId, MonitorConfig config) {
        if (log.isDebugEnabled()) {
            log.debug("Creating MonitorFacadeImpl for application " + applicationName + ", instance "
                    + instanceId);
        }

        this.applicationName = applicationName;
        this.instanceId = instanceId;
        this.configuration = config;

        if (log.isDebugEnabled()) {
            log.debug("Collection stategy for application " + applicationName + ", instance "
                    + instanceId + " is " + config.getCollectionStrategy());
        }

        try {
            Class collectionStrategyClass = Class.forName(config.getCollectionStrategy());
            collectionStrategy = (CollectionStrategy) collectionStrategyClass.newInstance();
            collectionStrategy.init(config);
        } catch (ClassNotFoundException e) {
            log.error("Error creating MonitorFacadeImpl for application " + applicationName
                    + ", instance " + instanceId, e);
        } catch (InstantiationException e) {
            log.error("Error creating MonitorFacadeImpl for application " + applicationName
                    + ", instance " + instanceId, e);
        } catch (IllegalAccessException e) {
            log.error("Error creating MonitorFacadeImpl for application " + applicationName
                    + ", instance " + instanceId, e);
        } finally {
            if (collectionStrategy == null) {
                collectionStrategy = new DoNothingCollectionStrategy();
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Created MonitorFacadeImpl for application " + applicationName + ", instance "
                    + instanceId + " with configuration " + config);
        }
    }

    public StatisticsCollector recordExecutionBegin(ExecutionTimer timer) {
        StatisticsCollector collector = getStatisticsCollectorOfThisThread();
        collector.recordExecutionBegin(timer);
        return collector;
    }

    public void recordExecutionEnd(ExecutionTimer timer) {
        recordExecutionEnd(timer, getStatisticsCollectorOfThisThread());
    }
    
    public void recordExecutionEnd(ExecutionTimer timer, StatisticsCollector collector) {
        collector.recordExecutionEnd(timer);
    }

    public boolean isMonitoringEnabled() {
        return getConfiguration().isMonitoringEnabled()
                && getConfiguration().isMonitoringEnabledForCurrentThread();
    }

    public MonitorConfig getConfiguration() {
        return configuration;
    }

    public String toString() {
        return "MonitorFacadeImpl (app = " + applicationName + ", instance = " + instanceId + ")";
    }
    
    public String getApplicationName() {
        return applicationName;
    }
    
    public String getInstanceId() {
        return instanceId;
    }
    
    public void destroy() {
        collectionStrategy.destroy();
    }
    
    CollectionStrategy getCollectionStrategy() {
        return collectionStrategy;
    }
    
    StatisticsCollector getStatisticsCollectorOfThisThread() {
        return (StatisticsCollector) statisticsCollector.get();
    }
}
