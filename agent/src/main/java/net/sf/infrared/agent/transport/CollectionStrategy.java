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
package net.sf.infrared.agent.transport;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.base.model.OperationStatistics;

/**
 * InfraRED uses 2 collection strategies for aggregating the performance
 * statistics - remote and local.
 * 
 * For either strategy, the statistics object collected at StatisticsCollector
 * is merged with the global Statistics object held in StatisticsMgr.
 * StatisticsMgr is located remotely wrt the target application, in case of
 * remote collection strategy necessitating the use of BufferedAggregator and
 * Periodic Flush Policy. A collection strategy groups an aggregator, forwarder &
 * flush policy. This is to make sure that only compatible versions of each are
 * used together.
 * 
 * @author kamal.govindraj
 */
public interface CollectionStrategy {
    /**
     * Initialize the collection strategy, this methods need to be overridden to
     * create the aggregator, forwarder & the flush policy and tie them together
     * 
     * @return
     */
    public boolean init(MonitorConfig configuration);

    /**
     * Needs to hanle the collection of statistic objects. This method will be
     * called after the execution of a request is complete
     * 
     * @param stats -
     *            statistics object that needs to be
     * @return true if the operation succeded other wise false
     */
    public boolean collect(OperationStatistics stats);

    /**
     * Suspend statistics collection
     */
    public void suspend();

    /**
     * Resume statistics collection
     */
    public void resume();

    /**
     * Destroy the strategy, bring the flush policy etc down.
     * 
     * @return
     */
    public boolean destroy();
}
