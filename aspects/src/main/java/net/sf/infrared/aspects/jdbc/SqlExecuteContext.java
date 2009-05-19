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
package net.sf.infrared.aspects.jdbc;

import java.util.Collections;
import java.util.List;

import net.sf.infrared.base.model.ExecutionContext;

/**
 * 
 * @author binil.thomas
 */
public class SqlExecuteContext implements ExecutionContext {
	private static final long serialVersionUID = 2019467082384847176L;
	private SqlContext parent;
	
	public SqlExecuteContext(SqlContext parent) {
		if (parent == null) {
			throw new IllegalArgumentException("parent SqlContext cannot be null");
		}
		this.parent = parent;
	}
	
	public SqlExecuteContext(String name, String layer) {
		
		SqlContext parentContext = new SqlContext(name, layer);
		this.parent = parentContext;
		this.parent.addChild(this);
		
	}
	
	// The string returned is stored in the DB as the name.
	public String getName() {
		return (parent == null) ? null : parent.getName();		
	}
	
	
	public ExecutionContext getParent() {
		return parent;
	}

	public String getLayer() {
        return "SQL"; //@TODO make constant!
	}

	public List getChildren() {
		return Collections.EMPTY_LIST;
	}

	public void addChild(ExecutionContext arg0) {
		throw new UnsupportedOperationException(); // @TODO for now
	}
	
	public String toString() {
		//return "[JDBC] EXECUTE (" + ((SqlContext) getParent()).getSql() + ")";
        return parent.getSql() + " [SQL] EXECUTE ";
	}
	
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (o == this) {
			return true;
		}
		
        if (!(o instanceof SqlExecuteContext)) {
            return false;
        }
		
		SqlExecuteContext other = (SqlExecuteContext) o;
		return other.parent.equals(parent);
	}

	public int hashCode() {
		return parent.hashCode();
	}
}
