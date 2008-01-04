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
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.agent.transport.impl;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.transport.Aggregator;
import net.sf.infrared.agent.transport.Forwarder;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**
 * 
 * @author binil.thomas
 */
public class PooledAggregator implements Aggregator {
    private static final Logger log = LoggingFactory.getLogger(PooledAggregator.class);

    private PooledExecutor executor;

    private Aggregator aggregator;

    public PooledAggregator(Aggregator aggregator, int bufferLength, int maxThreads) {
        this.aggregator = aggregator;

        executor = new PooledExecutor(new BoundedBuffer(bufferLength), maxThreads);
        executor.setKeepAliveTime(1000 * 60 * 5); // 5 minutes
        executor.discardOldestWhenBlocked();
    }

    public void aggregate(final OperationStatistics stats) {
        try {
            executor.execute(new Runnable() {
                public void run() {
                    aggregator.aggregate(stats);
                }
            });

            if (log.isDebugEnabled()) {
                log.debug("Scheduled " + stats + " for merging");
            }
        } catch (InterruptedException e) {
            log.error("Error in aggregate, ignoring", e);
        }
    }

    public void flush() {
        aggregator.flush();
    }

    public void setForwarder(Forwarder forwarder) {
        aggregator.setForwarder(forwarder);
    }
    
    public void shutdown() {
        executor.shutdownNow();
        aggregator.shutdown();
    }

	public boolean init(MonitorConfig configuration) {
		return true;
	}
}
