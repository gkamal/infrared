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


import java.util.ArrayList;
import java.util.List;

import net.sf.infrared.base.model.ExecutionContext;

/**
 * 
 * @author binil.thomas
 */
public class SqlContext implements ExecutionContext {
	private static final long serialVersionUID = 2479921818308143031L;
	private SqlPrepareContext prepare = null;
	private SqlExecuteContext execute = null;
	private String sql;
		
	private List children = new ArrayList(3);
	
	public SqlContext(String sql) {
		if (sql == null) {
			throw new IllegalArgumentException("sql string cannot be null");
		}
		this.sql = sql;
	}
	
	public SqlContext(String sql, String layer){
		this(sql);
	}
	
	public String getName() {		
		return sql;
	}
	
	
	public ExecutionContext getParent() {
		return null;
	}

	public String getLayer() {
		return "SQL";
	}

	public List getChildren() {
		return children;
	}

	public void addChild(ExecutionContext ctx) {
		if (children.contains(ctx))
			return;

		if (ctx instanceof SqlPrepareContext)
			this.prepare = (SqlPrepareContext) ctx;
		else if (ctx instanceof SqlExecuteContext)
			this.execute = (SqlExecuteContext) ctx;
		else
			throw new IllegalArgumentException("invalid child element for SqlContext");

		children.add(ctx);
	}
	
	public String toString() {
		return sql;
	}
	
	public boolean equals(Object o) {
		if (o == null) return false;
		
		if (this == o) return true;
		
		if (! (o instanceof SqlContext) ) return false;
		
		SqlContext other = (SqlContext) o;
		
		return this.sql.equals(other.sql) ;
	}

	public int hashCode() {
		return 13 * sql.hashCode();
	}
	
	public SqlPrepareContext getPrepareContext() {
        synchronized (this) {
            if (prepare == null) {
                prepare = new SqlPrepareContext(this);
                children.add(prepare);
            }
        }
		return prepare;
	}
	
	public SqlExecuteContext getExecuteContext() {
        synchronized (this) {
            if (execute == null) {
                execute = new SqlExecuteContext(this);
                children.add(execute);
            }
        }
		return execute;
	}
	
	public String getSql() {
		return sql;
	}
}
