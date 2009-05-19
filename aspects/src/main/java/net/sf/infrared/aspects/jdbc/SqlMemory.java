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
package net.sf.infrared.aspects.jdbc;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * This class 'remembers' the template SQL using which a PreparedStatement or CallableStatement
 * was prepared. (The JDBC interfaces, unfortunately, do not expose this capability).
 * 
 * @author prashant.nair
 * @author binil.thomas
 */
public class SqlMemory {
    // map of PreparedStatement implementation -> sql string
    private Map psToSqlMap = new WeakHashMap();
    
    public void memorizeSql(String sql, PreparedStatement ps) {
    	psToSqlMap.put(ps, sql);
    }
    
    // untyped version of the above api
    public void memorizeSql(String sql, Object ps) {
    	psToSqlMap.put(ps, sql);
    }
    
    public String recollectSql(PreparedStatement ps) {
    	return (String) psToSqlMap.get(ps);
    }
    
    // untyped version of the above api
    public String recollectSql(Object ps) {
    	return (String) psToSqlMap.get(ps);
    }
}
