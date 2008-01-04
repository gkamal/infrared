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
 * Contributor(s):   prashant.nair;
 *
 */
package net.sf.infrared.aspects.api;

import java.util.StringTokenizer;

import net.sf.infrared.aspects.AbstractExecutionContext;


/**
 * 
 * @author binil.thomas
 * @author prashant.nair
 */
public class ApiContext extends AbstractExecutionContext {

	private static final long serialVersionUID = 8725845370451073671L;

	public static final String DELIMITER = ":";
    
    private String methodName;

    private String className;

    public ApiContext(String className, String methodName, String layer) {
        super(layer);
        this.className = className;
        this.methodName = methodName;
    }

    public ApiContext(String name, String layer) {
        super(layer);

        if (name == null) {
            throw new IllegalArgumentException(
                    "The name argument for the ApiContext constructor cannot be null");
        }

        //@TODO why store classname and method name seperately? instead store name
        StringTokenizer st = new StringTokenizer(name, DELIMITER);
        if (st.countTokens() != 2) {
            throw new IllegalArgumentException(
                    "The name argument for the ApiContext constructor is invalid");
        }

        this.className = st.nextToken();
        this.methodName = st.nextToken();
    }

    public String getName() {
        return className + DELIMITER + methodName;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (!(o instanceof ApiContext)) {
            return false;
        }

        ApiContext other = (ApiContext) o;

        return this.methodName.equals(other.methodName) && this.getLayer().equals(other.getLayer())
        && this.className.equals(other.className);                
    }

    public int hashCode() {
        return 7 * methodName.hashCode() + 11 * className.hashCode() + 13 * getLayer().hashCode();
    }

    public String toString() {
        //return "[" + getLayer() + "] " + className + DELIMITER + methodName;
        return className + DELIMITER + methodName + " [" + getLayer() + "] ";
    }
}
