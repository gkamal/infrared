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
import net.sf.infrared.agent.transport.CollectionStrategy;
import net.sf.infrared.agent.transport.FlushPolicy;
import net.sf.infrared.agent.transport.Forwarder;
import net.sf.infrared.base.model.OperationStatistics;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * The CollectionStrategy impelementation, creates the Aggregator, Forwarder &
 * FlushPolicy appropriate for infraRED to send statistics collected to a
 * stand-alone infrared web application.
 * 
 * @author kamal.govindraj
 */
public class CentralizedCollectionStrategy implements CollectionStrategy {
    private static final String KEY_FLUSH_FREQUENCY = "collection-strategy.ccs.flush-frequency";
    
    private static final String KEY_POOL = "collection-strategy.ccs.pool";

    private static final String KEY_POOL_MAX_THREADS = "collection-strategy.ccs.pool.maxThreads";

    private static final String KEY_POOL_BUFFER_LENGTH = "collection-strategy.ccs.pool.buffer-length";

    private static final String KEY_REMOTE_HOST = "collection-strategy.ccs.remotehost";
    
    private static final String KEY_REMOTE_PORT = "collection-strategy.ccs.port";

    private static final String DEFAULT_REMOTE_HOST = "localhost";

    private static final int DEFAULT_REMOTE_PORT = 7777;
    
    private static final int DEFAULT_POOL_MAX_THREADS = 1;

    private static final int DEFAULT_POOL_BUFFER_LENGTH = 100;

    private static final Logger log = LoggingFactory.getLogger(CentralizedCollectionStrategy.class);

    private Aggregator aggregator = null;

    private Forwarder forwarder = null;

    private PeriodicFlushPolicy flushPolicy = null;

    private boolean suspended = false;

    public CentralizedCollectionStrategy() {
    }

    public boolean init(MonitorConfig configuration) {
        String hostName = configuration.getProperty(KEY_REMOTE_HOST, DEFAULT_REMOTE_HOST);
        int portNo = configuration.getProperty(KEY_REMOTE_PORT, DEFAULT_REMOTE_PORT);
        forwarder = new SocketForwarder(hostName, portNo);
        if (configuration.getProperty(KEY_POOL, false)) {
            log.debug("Using PooledAggregator for CentralCollectionStrategy");
            int length = configuration.getProperty(KEY_POOL_BUFFER_LENGTH, DEFAULT_POOL_BUFFER_LENGTH);
            int maxThreads = 
                configuration.getProperty(KEY_POOL_MAX_THREADS, DEFAULT_POOL_MAX_THREADS);
            aggregator = new PooledAggregator(new BufferedAggregator(), length, maxThreads);
        } else {
            log.debug("Using BufferedAggregator (not pooled) for CentralCollectionStrategy");
            aggregator = new BufferedAggregator();
        }
        aggregator.setForwarder(forwarder);
        aggregator.init(configuration);
        flushPolicy = new PeriodicFlushPolicy();
        long flushFrequency = configuration.getProperty(KEY_FLUSH_FREQUENCY, 
        		PeriodicFlushPolicy.DEFAULT_FREQUENCY);
        flushPolicy.setFrequency(flushFrequency);
        flushPolicy.setAggregator(aggregator);
        forwarder.init(false);

        if (log.isDebugEnabled()) {
            log.debug("Initialized CentralCollectionStrategy remote host = " + hostName
                    + " remote port = " + portNo);
        }

        return flushPolicy.activate();
    }

    public boolean collect(OperationStatistics stats) {
        if (!suspended) {
            aggregator.aggregate(stats);
            return true;
        } else {
            log.info("Ignoring - as stats collection suspended");
        }
        return false;
    }

    public void suspend() {
        suspended = true;
        forwarder.suspend();
    }

    public void resume() {
        suspended = false;
        forwarder.resume();
    }

    public Aggregator getAggregator() {
        return aggregator;
    }

    public boolean destroy() {
        flushPolicy.shutDown();
        aggregator.setForwarder(null);
        aggregator.shutdown();
        forwarder.destroy();
        aggregator = null;
        forwarder = null;
        return false;
    }

    FlushPolicy getFlushPolicy() {
        return flushPolicy;
    }

    Forwarder getForwarder() {
        return forwarder;
    }
}
