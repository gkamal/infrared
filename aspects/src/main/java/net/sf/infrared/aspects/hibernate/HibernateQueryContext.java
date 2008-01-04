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
package net.sf.infrared.aspects.hibernate;

import net.sf.infrared.aspects.AbstractExecutionContext;

/**
 *
 * @author binil.thomas
 */
public class HibernateQueryContext extends AbstractExecutionContext {
	private static final long serialVersionUID = 6188275156433668510L;
	private String query;
    
    public HibernateQueryContext(String query) {
        super("Hibernate");
        if (query == null) {
            throw new IllegalArgumentException("query string cannot be null");
        }
        this.query = query;
    }
    
    public HibernateQueryContext(String name, String layer) {
        super(layer);
        this.query = name;
    }
    
    public String getName() {
        return this.query;
    }
    
    public String toString() {
        return "Hibernate Query " + this.query;
    }
    
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (o == this) {
            return true;
        }
        
        if (! (o instanceof HibernateQueryContext) ) {
            return false;
        }
        
        HibernateQueryContext other = (HibernateQueryContext) o;
        
        return other.query.equals( this.query );
    }
    
    public int hashCode() {
        return 7 * query.hashCode();
    }
}
