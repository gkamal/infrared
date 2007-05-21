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
package net.sf.infrared.agent;

import org.apache.log4j.Logger;

import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.util.LoggingFactory;

/**
 * 
 * @author kamal.govindraj
 */
public class MultipleEntryGuard implements MonitorFacade {

    private static final Logger log = LoggingFactory.getLogger(MultipleEntryGuard.class);

    // private static final String KEY_JDBC_MONITORING_ENABLED = "jdbc-monitoring-enable";

    private static final long NO_OF_FATAL_ERRORS_TOLERATED = 10;

    private static final int THRESHOLD = 30;

    private MonitorFacade delegate;

    private int fatalErrors = 0;

    private long methodStartTime = 0;

    private ThreadLocal callInProgress = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return Boolean.FALSE;
        }
    };

    public MultipleEntryGuard(MonitorFacade delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delate MonitorFacade cannot be null");
        }
        this.delegate = delegate;
    }

    public StatisticsCollector recordExecutionBegin(ExecutionTimer timer) {
//        if (isCallInProgress()) {
//            return null;
//        }
        try {
//            startCall();
            return delegate.recordExecutionBegin(timer);
        } catch (Throwable t) {
            handleError();
            log.error("Exception generated from InfraRED", t);
            return null;
        } 
//        finally {
//            endCall();
//        }
    }
    
    public void recordExecutionEnd(ExecutionTimer timer) {
//        if (isCallInProgress()) {
//            return;
//        }
        try {
//            startCall();
            delegate.recordExecutionEnd(timer);
        } catch (Throwable t) {
            handleError();
            log.error("Exception generated from InfraRED", t);
        } 
//        finally {
//            endCall();
//        }
    }
    
    public void recordExecutionEnd(ExecutionTimer timer, StatisticsCollector collector) {
//        if (isCallInProgress()) {
//            return;
//        }
        try {
//            startCall();
            delegate.recordExecutionEnd(timer, collector);
        } catch (Throwable t) {
            handleError();
            log.error("Exception generated from InfraRED", t);
        } 
//        finally {
//            endCall();
//        }
    }

    public boolean isMonitoringEnabled() {
        return delegate.isMonitoringEnabled();
    }

    public MonitorConfig getConfiguration() {
        return delegate.getConfiguration();
    }

    private boolean isCallInProgress() {
        // We put only Boolean.TRUE and Boolean.FALSE in the threadlocal
        // so identity comparison against those will work
        return ((Boolean) callInProgress.get()) == Boolean.TRUE;
    }
    
    public String getApplicationName() {
        return delegate.getApplicationName();
    }
    
    public String getInstanceId() {
        return delegate.getInstanceId();
    }
    
    public void destroy() {
        delegate.destroy();
    }

    private void startCall() {
//        if (log.isDebugEnabled()) {
//            log.debug("\nInfraRED start on " + Thread.currentThread());
//        }
        callInProgress.set(Boolean.TRUE);
        //methodStartTime = System.currentTimeMillis();
    }

    private void endCall() {
    	/*
        long timeInInfraRed = System.currentTimeMillis() - methodStartTime;
        if (timeInInfraRed > THRESHOLD) {
            log.debug("The execution time in InfraRED is above the threshold");
        }*/
        //methodStartTime = 0;
        callInProgress.set(Boolean.FALSE);
//        if (log.isDebugEnabled()) {
//            log.debug("InfraRED end on " + Thread.currentThread() + "\n");
//        }
    }

    private void handleError() {
        fatalErrors++;
        if (fatalErrors >= NO_OF_FATAL_ERRORS_TOLERATED) {
            log.error("Error thresholds crossed, turning off infrared");
            // Turn off monitoring
            delegate.getConfiguration().enableMonitoring(false);
            delegate.getConfiguration().enableCallTracing(false);
            // delegate.getConfiguration().setProperty(KEY_JDBC_MONITORING_ENABLED, false);
        }
    }
}
