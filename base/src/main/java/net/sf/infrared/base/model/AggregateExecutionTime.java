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
 * Contributor(s):   prashant.nair, subin.p
 *
 */
package net.sf.infrared.base.model;

import java.io.Serializable;

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * Captures the aggregated timing information for a set of executions.
 * 
 * <p>
 * Each AggregateExecutionTime is created with an ExecutionContext - the
 * aggregated timing information represented will be of executions of that
 * context type. The ExecutionContext answers the question "what was executed?",
 * whereas this class answers the question "what is the timing information for
 * those executions?".
 * 
 * <p>
 * AggregateExecutionTime starts out empty when created; it gets more data as
 * other AggregateExecutionTimes and ExecutionTimers (representing one execution
 * of the context) gets merged onto it.
 * 
 * @author binil.thomas
 * @author prashant.nair
 */
public class AggregateExecutionTime implements Cloneable, Serializable {
    private static final Logger log = LoggingFactory.getLogger(AggregateExecutionTime.class);

    private ExecutionContext ctx;

    private int count = 0;

    private long totalInclusiveTime = 0;

    private long maxInclusiveTime = Long.MIN_VALUE;

    private long minInclusiveTime = Long.MAX_VALUE;

    private long totalExclusiveTime = 0;

    private long maxExclusiveTime = Long.MIN_VALUE;

    private long minExclusiveTime = Long.MAX_VALUE;

    private long timeOfFirstExecution = Long.MAX_VALUE;

    private long timeOfLastExecution = -1;

    private long inclusiveFirstExecutionTime = -1;

    private long inclusiveLastExecutionTime = -1;

    private long exclusiveFirstExecutionTime = -1;

    private long exclusiveLastExecutionTime = -1;
    
    private String layerName;

    /**
     * Creates a new AggregateExecutionTime object which can track timing
     * information of multiple executions of the given ExecutionContext
     */
    public AggregateExecutionTime(ExecutionContext ctx) {
        assert (ctx != null): "Cannot create AggregateExecutionTime with null ExecutionContext";
        this.ctx = ctx;
    }

    /**
     * Merges another AggregateExecutionTime onto this one.
     * 
     * <p>
     * The argument AggregateExecutionTime is left unchanged after this
     * operation. The target AggregateExecutionTime, after the merge operation,
     * will also represent the aggregated timing information of those of the
     * argument. For instance, the execution count will be the sum of the
     * original execution count and the execution count of the argument.
     * 
     * <p>
     * Only AggregateExecutionTimes representing the same ExecutionContext can
     * be merged - 'same' here means that the two ExecutionContext objects must
     * be equal as defined by their equals(Object) method. 
     */
    public void merge(AggregateExecutionTime aet) {
        assert (getContext().equals(aet.getContext())) : 
            "Illegal attempt to merge two AggregateExecutionTimes representing different" +
            " contexts[ " + getContext() + ", " + aet.getContext() + " ]"; 
            // will this string concatenation happen always or only when asserts are enabled?
        
        synchronized (this) {
            mergeExecutionCount(aet);
            mergeInclusiveTime(aet);
            mergeExclusiveTime(aet);
            mergeFirstExecution(aet);
            mergeLastExecution(aet);
            if (getLayerName() == null) {
                this.layerName = aet.getLayerName();
            }
            assert (getLayerName().equals(aet.getLayerName())) :
                "Illegal attempt to merge two AggregateExecutionTimes executed under two" +
                " layers[ " + getLayerName() + ", " + aet.getLayerName() + " ]";
        }

        if (log.isDebugEnabled()) {
            log.debug(this + " - Merged " + aet);
        }
    }

    /**
     * Adds the time of an execution to this one. The execution that is to be
     * merged is represented by an ExecutionTimer argument.
     * 
     * <p>
     * The argument ExecutionTimer is left unchanged after this operation. The
     * target AggregateExecutionTime, after the merge operation, will also
     * represent the timing information from the argument ExecutionTimer. For
     * instance, the exeucion count will be incremented by one.
     * 
     * <p>
     * Only ExecutionTimers representing the same ExecutionContext can be merged -
     * 'same' here means that the two ExecutionContext objects must be euqal as
     * defined by their equals(Object) method. 
     */
    public void merge(ExecutionTimer et) {
        assert (this.ctx.equals(et.getContext())) : 
            "Illegal attempt to merge an ExecutionTime representing a different" +
            " context[ " + et.getContext() + " ] than this one[ " + this.ctx + " ]"; 
        
        long inc = et.getInclusiveTime();
        long exc = et.getExclusiveTime();

        synchronized (this) {
            mergeExecutionCount(1);
            mergeInclusiveTime(inc, inc, inc);
            mergeExclusiveTime(exc, exc, exc);
            mergeFirstExecution(et.getStartTime(), inc, exc);
            mergeLastExecution(et.getStartTime(), inc, exc);
            if (getLayerName() == null) {
                setLayerName( et.getLayerName() );
            }
            assert (getLayerName().equals(et.getLayerName())) :
                "Illegal attempt to merge two AggregateExecutionTimes executed under two" +
                " layers[ " + getLayerName() + ", " + et.getLayerName() + " ]";
            
        }

        if (log.isDebugEnabled()) {
            log.debug(this + " - Merged " + et);
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("CloneNotSupportedException should never be thrown here", e);
            return null;
        }
    }

    public String toString() {
        return "AggregateExecutionTime for " + ctx + "[ total inclusive time = "
                + totalInclusiveTime + ", " + "total exclusive time = "
                + totalExclusiveTime + ", " + "execution count = " + count
                + " ]";
    }

    /**
     * Gets the ExecutionContext associated with this AggregateExecutionTime.
     * 
     * <p>
     * The timing information contained in this AggregateExecutionTime pertains
     * to multiple executions of whatever is represented by the returned
     * ExecutionContext.
     */
    public ExecutionContext getContext() {
        return ctx;
    }

    /**
     * Gets the total number of times the ExecutionContext was executed.
     * 
     * <p>
     * Returns 0 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public int getExecutionCount() {
        return count;
    }

    /**
     * Gets the system time the first (least recent) time the ExecutionContext
     * was executed.
     * 
     * <p>
     * Returns -1 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public long getTimeOfFirstExecution() {
        return timeOfFirstExecution;
    }

    /**
     * Gets the inclusive time of the first (least recent) execution of the
     * ExecutionContext.
     * 
     * <p>
     * Returns -1 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public long getInclusiveFirstExecutionTime() {
        return inclusiveFirstExecutionTime;
    }

    /**
     * Gets the exclusive time of the first (least recent) execution of the
     * ExecutionContext.
     * 
     * <p>
     * Returns -1 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public long getExclusiveFirstExecutionTime() {
        return exclusiveFirstExecutionTime;
    }

    /**
     * Gets the system time the last (most recent) time the ExecutionContext was
     * executed.
     * 
     * <p>
     * Returns -1 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public long getTimeOfLastExecution() {
        return timeOfLastExecution;
    }

    /**
     * Gets the inclusive time of the last (most recent) execution of the
     * ExecutionContext.
     * 
     * <p>
     * Returns -1 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public long getInclusiveLastExecutionTime() {
        return inclusiveLastExecutionTime;
    }

    /**
     * Gets the exclusive time of the last (most recent) execution of the
     * ExecutionContext.
     * 
     * <p>
     * Returns -1 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public long getExclusiveLastExecutionTime() {
        return exclusiveLastExecutionTime;
    }

    /**
     * Gets the total inclusive time of all executions of the ExecutionContext.
     * 
     * <p>
     * Returns 0 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public long getTotalInclusiveTime() {
        return totalInclusiveTime;
    }

    /**
     * Gets the total exclusive time of all executions of the ExecutionContext.
     * 
     * <p>
     * Returns 0 if this AggregateExecutionTime does not contain any timing
     * information yet.
     */
    public long getTotalExclusiveTime() {
        return totalExclusiveTime;
    }

    /**
     * Gets the maximum inclusive time taken of all executions of the
     * ExecutionContext.
     * 
     * <p>
     * Returns Long.MIN_VALUE if this AggregateExecutionTime does not contain
     * any timing information yet.
     */
    public long getMaxInclusiveTime() {
        return maxInclusiveTime;
    }

    /**
     * Gets the minimum inclusive time taken of all executions of the
     * ExecutionContext.
     * 
     * <p>
     * Returns Long.MAX_VALUE if this AggregateExecutionTime does not contain
     * any timing information yet.
     */
    public long getMinInclusiveTime() {
        return minInclusiveTime;
    }

    /**
     * Gets the maximum exclusive time taken of all executions of the
     * ExecutionContext.
     * 
     * <p>
     * Returns Long.MIN_VALUE if this AggregateExecutionTime does not contain
     * any timing information yet.
     */
    public long getMaxExclusiveTime() {
        return maxExclusiveTime;
    }

    /**
     * Gets the minimum exclusive time taken of all executions of the
     * ExecutionContext.
     * 
     * <p>
     * Returns Long.MAX_VALUE if this AggregateExecutionTime does not contain
     * any timing information yet.
     */
    public long getMinExclusiveTime() {
        return minExclusiveTime;
    }
    
    /**
     * Gets the average inclusive time of each execution of the ExecutionContext.
     * 
     * <p>
     * Returns 0 if this AggregateExecutionTime does not contain any 
     * timing information yet.
     */
    public double getAverageInclusiveTime() {
        if (getExecutionCount() == 0) {
            return 0;
        }
        return ((double) getTotalInclusiveTime()) / getExecutionCount();
    }

    /**
     * Gets the average exclusive time of each execution of the ExecutionContext.
     * 
     * <p>
     * Returns 0 if this AggregateExecutionTime does not contain any 
     * timing information yet.
     */
    public double getAverageExclusiveTime() {
        if (getExecutionCount() == 0) {
            return 0;
        }
        return ((double) getTotalExclusiveTime()) / getExecutionCount();
    }

    /**
     * Gets the adjusted average inclusive time of each execution of the ExecutionContext.
     * Adjusted average inclusive time means the average inclusive time calculated after 
     * excluding the first execution.
     *
     * <p>
     * Returns 0 if this AggregateExecutionTime does not contain any 
     * timing information yet. Returns the same value as returned by
     * getAverageInclusiveTime() if this ExecutionContext has been
     * executed only once.
     */
    public double getAdjAverageInclusiveTime() {
        if (getExecutionCount() <= 1) {
            return getAverageInclusiveTime();
        }
        else {
            return (double) (getTotalInclusiveTime() - getInclusiveFirstExecutionTime())
                                                            / (getExecutionCount() - 1);
        }
    }

    /**
     * Gets the adjusted average exclusive time of each execution of the ExecutionContext.
     * Adjusted average exclusive time means the average exclusive time calculated after 
     * excluding the first execution.
     *
     * <p>
     * Returns 0 if this AggregateExecutionTime does not contain any 
     * timing information yet. Returns the same value as returned by
     * getAverageExclusiveTime() if this ExecutionContext has been
     * executed only once.
     */
    public double getAdjAverageExclusiveTime() {
        if (getExecutionCount() <= 1) {
            return getAverageExclusiveTime();
        }
        else {
            return (double) (getTotalExclusiveTime() - getExclusiveFirstExecutionTime())
                                                           / (getExecutionCount() - 1);
        }
    }
    
    /**
     * Gets the name of the ExecutionContext.
     */
    public String getName(){
        return getContext().getName();
    }    

    void mergeExecutionCount(AggregateExecutionTime aet) {
        mergeExecutionCount(aet.getExecutionCount());
    }

    void mergeExecutionCount(int c) {
        count += c;
    }

    void mergeTotalTimes(AggregateExecutionTime aet) {
        mergeTotalTimes(aet.getTotalInclusiveTime(), aet.getTotalExclusiveTime());
    }

    void mergeTotalTimes(long totalInclusiveTime, long totalExclusiveTime) {
        this.totalInclusiveTime += totalInclusiveTime;
        this.totalExclusiveTime += totalExclusiveTime;
    }

    void mergeFirstExecution(AggregateExecutionTime aet) {
        mergeFirstExecution(aet.timeOfFirstExecution,
                aet.inclusiveFirstExecutionTime,
                aet.exclusiveFirstExecutionTime);
    }

    void mergeFirstExecution(long timeOfFirstExecution, long inclusiveTime, long exclusiveTime) {
        if (timeOfFirstExecution < this.timeOfFirstExecution) { // the other one executed earlier
            this.timeOfFirstExecution = timeOfFirstExecution;
            inclusiveFirstExecutionTime = inclusiveTime;
            exclusiveFirstExecutionTime = exclusiveTime;
        }
    }

    void mergeLastExecution(AggregateExecutionTime aet) {
        mergeLastExecution(aet.getTimeOfLastExecution(), 
                aet.getInclusiveLastExecutionTime(), aet.getExclusiveLastExecutionTime());
    }

    void mergeLastExecution(long timeOfLastExecution, long inclusiveTime,
            long exclusiveTime) {
        if (this.timeOfLastExecution < timeOfLastExecution) { // the other one last executed later
            this.timeOfLastExecution = timeOfLastExecution;
            inclusiveLastExecutionTime = inclusiveTime;
            exclusiveLastExecutionTime = exclusiveTime;
        }
    }

    void mergeInclusiveTime(AggregateExecutionTime e) {
        mergeInclusiveTime(e.getMaxInclusiveTime(), 
                e.getMinInclusiveTime(), e.getTotalInclusiveTime());
    }

    void mergeInclusiveTime(long max, long min, long total) {
        minInclusiveTime = Math.min(minInclusiveTime, min);
        maxInclusiveTime = Math.max(maxInclusiveTime, max);
        totalInclusiveTime += total;
    }

    void mergeExclusiveTime(AggregateExecutionTime e) {
        mergeExclusiveTime(e.getMaxExclusiveTime(), 
                e.getMinExclusiveTime(), e.getTotalExclusiveTime());
    }

    void mergeExclusiveTime(long max, long min, long total) {
        minExclusiveTime = Math.min(minExclusiveTime, min);
        maxExclusiveTime = Math.max(maxExclusiveTime, max);
        totalExclusiveTime += total;
    }

    public void setExclusiveFirstExecutionTime(long exclusiveFirstExecutionTime) {
        this.exclusiveFirstExecutionTime = exclusiveFirstExecutionTime;
    }

    public void setExclusiveLastExecutionTime(long exclusiveLastExecutionTime) {
        this.exclusiveLastExecutionTime = exclusiveLastExecutionTime;
    }

    public void setInclusiveFirstExecutionTime(long inclusiveFirstExecutionTime) {
        this.inclusiveFirstExecutionTime = inclusiveFirstExecutionTime;
    }

    public void setInclusiveLastExecutionTime(long inclusiveLastExecutionTime) {
        this.inclusiveLastExecutionTime = inclusiveLastExecutionTime;
    }

    public void setMaxExclusiveTime(long maxExclusiveTime) {
        this.maxExclusiveTime = maxExclusiveTime;
    }

    public void setMaxInclusiveTime(long maxInclusiveTime) {
        this.maxInclusiveTime = maxInclusiveTime;
    }

    public void setMinExclusiveTime(long minExclusiveTime) {
        this.minExclusiveTime = minExclusiveTime;
    }

    public void setMinInclusiveTime(long minInclusiveTime) {
        this.minInclusiveTime = minInclusiveTime;
    }

    public void setTimeOfFirstExecution(long timeOfFirstExecution) {
        this.timeOfFirstExecution = timeOfFirstExecution;
    }

    public void setTimeOfLastExecution(long timeOfLastExecution) {
        this.timeOfLastExecution = timeOfLastExecution;
    }

    public void setTotalExclusiveTime(long totalExclusiveTime) {
        this.totalExclusiveTime = totalExclusiveTime;
    }

    public void setTotalInclusiveTime(long totalInclusiveTime) {
        this.totalInclusiveTime = totalInclusiveTime;
    }

    public void setExecutionCount(int c) {
        this.count = c;
    }
    
    public void setContext(ExecutionContext context) {
        this.ctx = context;
    }    
     
    /**
     * Gets the name of the layer to which the ExecutionContext falls under
     */
    public String getLayerName() {
        return this.layerName;
    }
    
    public void setLayerName(String layer) {
        this.layerName = layer;                
    }
}
