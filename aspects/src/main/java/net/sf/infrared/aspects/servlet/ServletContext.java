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
package net.sf.infrared.aspects.servlet;

import net.sf.infrared.aspects.AbstractExecutionContext;

public class ServletContext extends AbstractExecutionContext {
    
    private String uri;
    
    public ServletContext(String uri) {
        super("HTTP");
        this.uri = uri;        
    }
    
    public ServletContext(String name, String layer) {
        super(layer);
        this.uri = name;
    }
    
    public String getName() {
        return uri;
    }
    
    public String toString() {
        return "Servlet " + getName();
    }
    
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (o == this) {
            return true;
        }
        
        if (! (o instanceof ServletContext) ) {
            return false;
        }
        
        ServletContext other = (ServletContext) o;
        
        return other.getName().equals( this.getName() );
    }
    
    public int hashCode() {
        return 7 * getName().hashCode();
    }
}
