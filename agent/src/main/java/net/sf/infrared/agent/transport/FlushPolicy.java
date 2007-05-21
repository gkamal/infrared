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

/**
 * Defines the interface for different flush policies, to provide a mechanism
 * for pushing data to the central store.
 * 
 * @author kamal.govindraj
 */
public interface FlushPolicy {

    /**
     * Trigger to activate the flush policy
     */
    public boolean activate();

    /**
     * Shutdown
     */
    public boolean shutDown();

    /**
     * Aggregator which needs to be informed to flush the data.
     * 
     * @param aggregator
     */
    public void setAggregator(Aggregator aggregator);

    /**
     * Returns if the flush policy is active.
     * 
     * @return
     */
    boolean isActive();
}
