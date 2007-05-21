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
 * Interface for implementing the different aggregation strategies
 * 
 * @author kamal.govindraj
 */
public interface Aggregator {
    /**
     * Initialize the Aggregator, this methods need to be overridden to initialize the
     * properties required in ApplicationStatistics 
     * @return
     */
    public boolean init(MonitorConfig configuration);
    
    /**
     * Aggregate statistics from a new request
     * 
     * @param stats
     */
    public void aggregate(OperationStatistics stats);

    /**
     * Flush the collected stats onto the Forwarder
     */
    public void flush();

    /**
     * The forwarder to which the collected statistics need to be forwarded
     * 
     * @param forwarder
     */
    public void setForwarder(Forwarder forwarder);
    
    public void shutdown();
}
