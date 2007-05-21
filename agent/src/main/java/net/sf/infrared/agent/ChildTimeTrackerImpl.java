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

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * An array based implementation, this approach is more complex but better than
 * the stack based approach as it creates less Objects for the normal cases. The
 * stack based approach has to work with immutable Long objects every update
 * results in throwing away the existing value and creating a new object.
 * 
 * @author kamal.govindraj
 */
public class ChildTimeTrackerImpl implements ChildTimeTracker {
    private static final int INITIAL_SIZE = 25;

    private static final int GROW_BY = INITIAL_SIZE / 4;

    private static final Logger log = LoggingFactory.getLogger(ChildTimeTrackerImpl.class);

    private long[] childTimes = null;

    private int capacity = 0;

    private int top = 0;

    public void begin() {
        if (top >= capacity) {
            growArray();
        }

        childTimes[top++] = 0;
    }

    public void recordChildExecutionTime(long time) {
        if (top > 1) {
            childTimes[top - 2] += time;
        }
    }

    public long getChildExecutionTime() {
        // Just a defensive check
        // There might be cases where monitoring is turned on
        // mid request, this might lead to mismatched begin/ end calls
        if (top > 0) {
            return childTimes[top - 1];
        }
        if (log.isDebugEnabled()) {
            log.debug("Mismatch in being/end calls");
        }
        return 0;
    }

    public void end() {
        if (top > 0) {
            top--;
        }

        shrinkIfRequired();
    }

    public void reset() {
        top = 0;
        shrinkIfRequired();
    }

    private void shrinkIfRequired() {
        if ((capacity > INITIAL_SIZE) && ((capacity - top) > (GROW_BY * 2))) {
            if (log.isInfoEnabled()) {
                log.info("Shrinking array capacity " + capacity + " top " + top);
            }

            long[] temp = childTimes;
            capacity = capacity - GROW_BY;
            childTimes = new long[capacity];

            for (int i = 0; i < top; i++) {
                childTimes[i] = temp[i];
            }
        }
    }

    private void growArray() {
        if (capacity > 0) {
            long[] temp = childTimes;
            childTimes = new long[capacity + GROW_BY];

            for (int i = 0; i < temp.length; i++) {
                childTimes[i] = temp[i];
            }

            capacity = capacity + GROW_BY;

            if (log.isInfoEnabled()) {
                log.info("Growing array by " + GROW_BY + " capacity " + capacity + " top " + top);
            }
        } else {
            childTimes = new long[INITIAL_SIZE];
            capacity = INITIAL_SIZE;

            if (log.isInfoEnabled()) {
                log.info("Initizliaing array with initial size " + INITIAL_SIZE);
            }
        }
    }
}
