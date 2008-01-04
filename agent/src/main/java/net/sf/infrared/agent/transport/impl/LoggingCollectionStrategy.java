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
 * Original Author: binil.thomas (Tavant Technologies) 
 * Contributor(s):  -;
 *
 */
package net.sf.infrared.agent.transport.impl;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.transport.Aggregator;
import net.sf.infrared.agent.transport.CollectionStrategy;
import net.sf.infrared.agent.transport.FlushPolicy;
import net.sf.infrared.agent.transport.Forwarder;
import net.sf.infrared.base.model.OperationStatistics;

/**
 * 
 * @author binil.thomas
 */
public class LoggingCollectionStrategy implements CollectionStrategy {

    private Aggregator aggregator = null;

    private Forwarder forwarder = null;

    private FlushPolicy flushPolicy = null;

    public boolean init(MonitorConfig configuration) {
        forwarder = new LoggingForwarder();

        aggregator = new PooledAggregator(new BufferedAggregator(), 100, 1);
        aggregator.setForwarder(new LoggingForwarder());
        aggregator.init(configuration);

        flushPolicy = new PeriodicFlushPolicy();
        flushPolicy.setAggregator(aggregator);

        forwarder.init(false);
        return flushPolicy.activate();
    }

    public boolean collect(OperationStatistics stats) {
        aggregator.aggregate(stats);
        return true;
    }

    public void suspend() {
    	//ignore
    }

    public void resume() {
    	//ignore
    }

    public boolean destroy() {
        flushPolicy.shutDown();
        aggregator.setForwarder(null);
        forwarder.destroy();
        aggregator = null;
        forwarder = null;
        return false;
    }

}
