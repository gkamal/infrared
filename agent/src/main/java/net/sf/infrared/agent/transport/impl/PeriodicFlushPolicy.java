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

import java.util.Timer;
import java.util.TimerTask;

import net.sf.infrared.agent.transport.Aggregator;
import net.sf.infrared.agent.transport.FlushPolicy;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

public class PeriodicFlushPolicy implements FlushPolicy {
    public static final long DEFAULT_FREQUENCY = 30 * 1000L;
    
    private static Logger log = LoggingFactory.getLogger(PeriodicFlushPolicy.class);
    
    private Aggregator aggregator;
    
    private long frequency = DEFAULT_FREQUENCY;
    
    private Timer timer = null;
    
    public PeriodicFlushPolicy() {}
    
    public PeriodicFlushPolicy(long frequency, Aggregator aggregator) {
		this.frequency = frequency;
		this.aggregator = aggregator;
	}



	public boolean activate() {
        if (isActive()) {
            return false;
        }
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
           public void run() {
               if (aggregator != null) {                   
                   aggregator.flush();
               }
           }
        }, 0, frequency);
        return true;
    }

    public boolean shutDown() {
        if (!isActive()) {
            return false;
        }
        timer.cancel();
        timer = null;
        return true;
    }

    public void setAggregator(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    public boolean isActive() {
        return (timer != null);
    }
    
    /**
     * Sets the frequency in which this polciy should flush the aggregator.
     * 
     * @param time
     *            the frequency in milliseconds
     */
    public void setFrequency(long time) {
        if (time <= 0) {
            log.error("Attempt to set frequency to " + time + "ms ignored. "
                    + "Flush frequency should be greater than zero ms");
            return;
        }
        this.frequency = time;
        if (log.isDebugEnabled()) {
            log.debug("Flush frequency set to " + time + " ms");
        }
    }

    /**
     * Gets the frequency in which this policy is set to flush the associated
     * aggregator. A newly created policy will return the public constant
     * specified by DEFAULT_FREQUENCY.
     * 
     * @return
     */
    public long getFrequency() {
        return frequency;
    }
}
