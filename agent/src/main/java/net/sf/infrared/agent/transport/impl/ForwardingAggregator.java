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
package net.sf.infrared.agent.transport.impl;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.transport.Aggregator;
import net.sf.infrared.agent.transport.Forwarder;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * An implementation of the aggregator which just passes on the stats object to
 * the forwarder without any processing
 * 
 * @author kamal.govindraj
 */
public class ForwardingAggregator implements Aggregator {
    private static final Logger log = LoggingFactory.getLogger(ForwardingAggregator.class);

    private Forwarder forwarder = null;

    public void aggregate(OperationStatistics stats) {
        if (forwarder != null) {
            forwarder.forward(stats);
            if (log.isDebugEnabled()) {
                log.debug("Forwarded statistics for " + stats.getApplicationName() + " on "
                        + stats.getInstanceId());
            }
        } else {
            log.error("forwarder not set correctly!");
        }
    }

    public void flush() {
        // no op
    }

    public void setForwarder(Forwarder forwarder) {
        this.forwarder = forwarder;
    }
    
    public void shutdown() {        
    }

	public boolean init(MonitorConfig configuration) {
		return false;
	}
}
