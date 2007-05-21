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

import org.apache.log4j.Logger;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.transport.Aggregator;
import net.sf.infrared.agent.transport.Forwarder;
import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.LoggingFactory;

/**
 * Implementation of the aggregator which merges each update with a local
 * statistics instance and flushes the aggregated statistics in response to a
 * flush.
 * 
 * @author kamal.govindraj
 */
public class BufferedAggregator implements Aggregator {
    private static final Logger log = LoggingFactory.getLogger(BufferedAggregator.class);

    private Forwarder forwarder;

    private ApplicationStatistics bufferStatistics;
    
    private int maxLastInvocations = 5;

    public BufferedAggregator() {
    }
    
	public boolean init(MonitorConfig configuration) {
		maxLastInvocations = configuration.getNoOfLastInvocationsToBeTracked();
		return true;
	}

    public void aggregate(OperationStatistics stats) {
        log.info("BufferedAggregator.aggregate");
        synchronized (this) {
            if (bufferStatistics == null) {
                bufferStatistics = 
                        new ApplicationStatistics(stats.getApplicationName(), stats.getInstanceId());
                bufferStatistics.setMaxLastInvocations(maxLastInvocations);
                log.info("Create new statistics object");
            }
            bufferStatistics.merge(stats);
            log.info("Merged statistics");
        }
    }

    public void flush() {
        ApplicationStatistics statsToFlush = null;
        synchronized (this) {
            if (this.bufferStatistics != null) {
                statsToFlush = this.bufferStatistics;
                this.bufferStatistics = null;
            } else {
                if (this.bufferStatistics == null && log.isDebugEnabled()) {
                    log.debug("No stats to send to collector; ignoring flush");
                }
                return;
            }
        }
        // Moved the forward call out of the synchorinzed block
        // as this can block and hold up the aggregate calls
        if (this.forwarder != null) {
            if (statsToFlush != null) {
                this.forwarder.forward(statsToFlush);
                statsToFlush.reset();
            }
        } else {
            log.error("Forwarder not initialized");
        }
    }

    public void setForwarder(Forwarder forwarder) {
        this.forwarder = forwarder;
    }
    
    public void shutdown() {        
    }


}
