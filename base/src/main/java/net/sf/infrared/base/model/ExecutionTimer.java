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
 * Original Author:  binil.thoms (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.base.model;

import java.io.Serializable;

/**
 * Timer used to time an execution. Precision is of millisecond order.
 *
 * <p>
 * ExecutionTimers are created with an ExecutionContext, which represents the execution that
 * this timer times. start(), stop() methods are called before and after the execution.
 * <p>
 * This timer calculates the time that elapsed between start() and stop() calls. This is the
 * inclusive time of the execution i.e. time which includes time spend in other executions that
 * happened as part of this execution. setExclusiveTime() method should be used to set the
 * time taken exclusively by an execution i.e. time spend <strong>only</strong> in this execution
 * ignoring time spend in other executions which happened as part of this one.
 *
 * @author binil.thomas
 */
public class ExecutionTimer implements Serializable {
    private ExecutionContext ctx;

    private long startTime = -1;

    private long endTime = -1;

    private long inclusiveTime = 0;

    private long exclusiveTime = 0;

    private String layerName;

    /**
     * Creates a new timer to time the execution represented by the given ExecutionContext.
     *
     * @param ctx
     */
    public ExecutionTimer(ExecutionContext ctx) {
        this.ctx = ctx;
    }

    ExecutionTimer() {
    }

    /**
     * Starts the timer.
     *
     * @throws IllegalArgumentException if start() is already called.
     */
    public void start() {
        assert !isExecuting() : "Cannot start an already started ExecutionTimer";
        startTime = getCurrentTime();
    }

    /**
     * Stops the timer.
     *
     * @throws IllegalArgumentException if start() is not called yet, or if a start() & stop() pair
     * had been called already
     */
    public void stop() {
        assert isExecuting(): "Cannot stop an ExecutionTimer which hasnt started";
        endTime = getCurrentTime();
        inclusiveTime = endTime - startTime;
    }

    /**
     * Sets the exclusive time.
     *
     * <p>
     * Exclusive time is the time spend <strong>only</strong> in this execution,
     * ignoring time spend in other executions which happened as part of this one.
     *
     * @throws IllegalArgumentException if an attempt is made to set exclsuive time
     * greater than inclusive time.
     */
    public void setExclusiveTime(long time) {
        assert (time <= inclusiveTime) : "exclusive time " + time +
                    " cannot be greater than inclusive time " + inclusiveTime;
        this.exclusiveTime = time;
    }

    /**
     * Gets the inclusive time.
     *
     * <p>
     * Inclusive time is the time that elapses between a pair of start() & stop() calls.
     */
    public long getInclusiveTime() {
        return inclusiveTime;
    }

    /**
     * Gets the system time when stop() was called.
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Gets the associated ExecutionContext
     */
    public ExecutionContext getContext() {
        return ctx;
    }

    /**
     * Gets the exclusive time.
     */
    public long getExclusiveTime() {
        return exclusiveTime;
    }

    /**
     * Gets the system time when start() was called.
     */
    public long getStartTime() {
        return startTime;
    }

    public String toString() {
        return "ExecutionTime for " + ctx +
            "(inclusive time = " + inclusiveTime + ", " +
            "exclusive time = " + exclusiveTime + ")";
    }

    public String getLayerName() {
        return this.layerName;
    }

    public void setLayerName(String layer) {
        this.layerName = layer;
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    boolean isExecuting() {
        return startTime != -1 && endTime == -1;
    }

    boolean hasFinishedExecution() {
        return endTime != -1;
    }

    void setInclusiveTime(long time) {
        this.inclusiveTime = time;
    }
}
