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
package net.sf.infrared.aspects;

import java.util.Collections;
import java.util.List;

import net.sf.infrared.base.model.ExecutionContext;

public abstract class AbstractExecutionContext implements ExecutionContext {

    private String layer;
    
    public AbstractExecutionContext(String layer) {
        this.layer = layer.intern();
    }
    
    public ExecutionContext getParent() {
        return null;
    }

    public String getLayer() {
        return layer;
    }

    public List getChildren() {
        return Collections.EMPTY_LIST;
    }

    public void addChild(ExecutionContext arg0) {
        throw new UnsupportedOperationException("Can't add children to " + this);
    }
}
