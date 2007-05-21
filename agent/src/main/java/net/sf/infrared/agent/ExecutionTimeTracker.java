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
package net.sf.infrared.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.infrared.base.model.ExecutionTimer;

/**
 * 
 * @author binil.thomas
 */
public class ExecutionTimeTracker {
    private Map executions = new HashMap();

    public void recordExecution(String layer, ExecutionTimer timer) {
        timer.setLayerName(layer);
        LinkedList l = (LinkedList) executions.get(layer);
        if (l == null) {
            l = new LinkedList();
            executions.put(layer, l);
        }
        l.addFirst(timer);
    }

    public Map reset() {
        Map oldExecutions = executions;
        executions = new HashMap();
        return oldExecutions;
    }
}
