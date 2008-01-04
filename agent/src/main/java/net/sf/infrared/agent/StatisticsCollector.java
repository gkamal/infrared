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
 * Original Author: kamal.govindraj (Tavant Technologies) 
 * Contributor(s):  binil.thomas
 *
 */
package net.sf.infrared.agent;

import java.util.Date;
import java.util.Map;

import net.sf.infrared.agent.transport.CollectionStrategy;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.base.util.Tree;

import org.apache.log4j.Logger;

/**
 * 
 * @author kamal.govindraj
 * @author binil.thomas
 */
public class StatisticsCollector {
    private static final Logger log = LoggingFactory.getLogger(StatisticsCollector.class);
    
    private static final Logger healthLog = LoggingFactory.getLogger("net.sf.infrared.agent.health");

    private ConfigCache configuration = null;

    private String applicationName = "all applications";

    private String instanceId = "unknown instance";

    private CollectionStrategy collectionStrategy = null;

    private ChildTimeTracker childTracker = new ChildTimeTrackerImpl();

    private LayerTimeTracker layerTracker = new LayerTimeTracker();

    private TreeBuilder treeBuilder = new TreeBuilder();

    private ExecutionTimeTracker executionTracker = new ExecutionTimeTracker();

    private int depth = 0;
    
    private long startTime = -1;
    
    private long endTime = -1;
    
    private int numOfExecutionsTracked = 0;
    
    private int numOfExecutionsIgnored = 0;

    private boolean callInProgress = false;

    public StatisticsCollector(CollectionStrategy collectionStrategy, String applicationName,
            String instanceId, MonitorConfig configuration) {

        this.collectionStrategy = collectionStrategy;
        this.applicationName = applicationName;
        this.instanceId = instanceId;
        this.configuration = new ConfigCache(configuration);

        if (log.isDebugEnabled()) {
            log.debug("Created Statistics Collector for application " + applicationName
                    + " on host " + instanceId + " thread " + Thread.currentThread());
        }
    }

    StatisticsCollector() {
    }

    public void recordExecutionBegin(ExecutionTimer timer) {
        if (callInProgress) {
            return;
        }
        callInProgress = true;
        begin(timer);
        timer.start();        
        callInProgress = false;
    }
    
    void begin(ExecutionTimer timer) {
        incrementDepthCount();

        childTracker.begin();
        layerTracker.enterLayer(timer);
        if (isCallTracingEnabled()) {
            treeBuilder.begin(timer);
        }
        
        if (log.isDebugEnabled()) {
            log.debug(this + " - Recording beginining of execution of " + timer.getContext()
                    + " at depth " + depth);
        }
    }

    public void recordExecutionEnd(ExecutionTimer timer) {     
        if (callInProgress) {
            return;
        }
        callInProgress = true;
        timer.stop();
        end(timer);
        callInProgress = false;
    }

    void end(ExecutionTimer timer) {
        if (log.isDebugEnabled()) {
            log.debug(this + " - Recorded end of execution of " + timer.getContext()
                    + " at depth " + depth);
        }

        setExclusiveTime(timer);

        boolean ok = true;
        if (isCallTracingEnabled()) {
            ok = treeBuilder.end(timer) && ok;
        }
        if (timer.getInclusiveTime() > getPruneBelowTime()) {
            executionTracker.recordExecution(layerTracker.getCurrentLayer(), timer);            
            // @TODO bug!! Layer time of those ignored executions will be recorded
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Discarded tracking execution of " + timer.getContext() + 
                        " because the time (" + timer.getInclusiveTime() + 
                        ") <= prune threshold (" + getPruneBelowTime() + ")");
            }
        }
        ok = ok && layerTracker.leaveLayer(timer);
        childTracker.end();
        
        ok = ok && decrementDepthCount();

        if (!ok) {
            log.error(this + " - Mismatch detected in begin/end calls, "
                    + "dumping stats collected so far");
            dumpStatsAndResetTrackers();
        }
    }
    
    public CollectionStrategy getCollectionStrategy() {
        return collectionStrategy;
    }

    public void setCollectionStrategy(CollectionStrategy cs) {
        this.collectionStrategy = cs;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String name) {
        this.applicationName = name;
    }

    public String getHostName() {
        return instanceId;
    }

    public void setHostName(String host) {
        this.instanceId = host;
    }

    public String toString() {
        return "StatisticsCollector[" + getApplicationName() + " on " + getHostName()
                + ", thread = " + Thread.currentThread() + "]";
    }

    void setExclusiveTime(ExecutionTimer timer) {
        long inclusiveTime = timer.getInclusiveTime();
        long exclusiveTime = inclusiveTime;

        if (inclusiveTime > getPruneBelowTime()) {
            childTracker.recordChildExecutionTime(inclusiveTime);
            exclusiveTime = inclusiveTime - childTracker.getChildExecutionTime();
            numOfExecutionsTracked++;
        } else {
            numOfExecutionsIgnored++;
        }
        timer.setExclusiveTime(exclusiveTime);
    }

    void incrementDepthCount() {
        if (depth == 0) {
            // This is to ensure that the value of pruneBelowTime and callTracing is 
            // not stale. It is done once when the depth is 0 which means that a new
            // request has just started.
            if (configuration != null) {
                configuration.refresh();
            }
            treeBuilder.setPruneBelowTime(getPruneBelowTime());
            layerTracker.setPruneBelowTime(getPruneBelowTime());
            startTime = System.currentTimeMillis();
        }
        depth++;
    }

    boolean decrementDepthCount() {
        depth--;

        if (depth < 0) {
            log.error("Depth count should have been positive, it is now " + depth);
            return false;
        }

        if (depth == 0) {
            endTime = System.currentTimeMillis();
            if (log.isDebugEnabled()) {
                log.debug(this + " - Reached end of an operation; collecting stats "
                        + "and resetting trackers");
            }            
            collectStatsAndResetTrackers();            
        }
        return true;
    }

    long getPruneBelowTime() {
        return configuration.pruneBelowTime();
    }

    boolean isCallTracingEnabled() {
        return configuration.callTracing();
    }

    void collectStatsAndResetTrackers() {      
        childTracker.reset();
        Map executionTimings = executionTracker.reset();
        Map layerTimings = layerTracker.reset(false);
        Tree requestTree = null;
        if (isCallTracingEnabled()) {
            requestTree = treeBuilder.reset();
        }

        OperationStatistics stats = createOperationStats(executionTimings, layerTimings,
                requestTree);
        getCollectionStrategy().collect(stats);
        logAgentHealth();
        startTime = endTime = -1;
        numOfExecutionsTracked = numOfExecutionsIgnored = 0;
    }

    OperationStatistics createOperationStats(Map executionTimings, Map layerTimings,
            Tree requestTree) {
        OperationStatistics operationStats = 
                new OperationStatistics(getApplicationName(), getHostName());
        operationStats.setExecutionTimes(executionTimings);
        operationStats.setLayerTimes(layerTimings);
        operationStats.setOperationTree(requestTree);
        operationStats.setApplicationName(applicationName);
        operationStats.setInstanceId(instanceId);
        operationStats.setStartTime(startTime);
        operationStats.setEndTime(endTime);
        operationStats.setNumOfExecutions(numOfExecutionsTracked, numOfExecutionsIgnored);
        return operationStats;
    }
    
    void logAgentHealth() {
        if (healthLog.isDebugEnabled()) {
            Date startDate = new Date(startTime);
            Date endDate = new Date(endTime);
            healthLog.debug("Agent collected stats for operation that started at " + startDate
                    + " and ended at " + endDate + ". Tracked " + numOfExecutionsTracked 
                    + " and ignored " + numOfExecutionsIgnored);
        }
    }

    void dumpStatsAndResetTrackers() {
        layerTracker.reset(true);
        childTracker.reset();
        treeBuilder.reset();
        executionTracker.reset();
    }
}

/**
 * The ConfigCache object holds a cached copy of frequently used configurations in MonitorConfig.
 * Each StatisticsCollector holds a ConfigCache object. At the start of a request the
 * StatisticsCollector refreshes its ConfigCache; changes made in the MonitorConfig
 * during the execution of a request are ignored by the StatisticsCollector.
 */
class ConfigCache {
    private boolean callTracing;
    private long pruneBelowTime;
    private MonitorConfig cfg;

    public ConfigCache(MonitorConfig cfg) {
        this.cfg = cfg;
    }

    public boolean callTracing() {
        return callTracing;
    }

    public long pruneBelowTime() {
        return pruneBelowTime;
    }

    public void refresh() {
        pruneBelowTime = cfg.getPruneThreshold();
        callTracing = cfg.isCallTracingEnabled();
    }
}
