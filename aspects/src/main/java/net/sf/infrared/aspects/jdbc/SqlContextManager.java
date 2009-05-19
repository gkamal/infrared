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

import java.util.Map;
import java.util.WeakHashMap;

/**
 * This class manages the SqlContext, SqlExecuteContext and SqlPrepareContext objects
 * pertaining to a given SQL string.
 * 
 * @author prashant.nair
 * @author binil.thomas
 */
public class SqlContextManager {
    // map of sql string -> SqlContext object
    private Map sqlStringToSqlContextMap = new WeakHashMap();
	
    public SqlContext getSqlContext(String sql) {
        SqlContext sqlCtx = (SqlContext) sqlStringToSqlContextMap.get(sql);
        synchronized (sqlStringToSqlContextMap) {
            if (sqlCtx == null) {
                sqlCtx = new SqlContext(sql);
                sqlStringToSqlContextMap.put(sql, sqlCtx);
            }
        }
        return sqlCtx;
    }

    public SqlExecuteContext getExecuteContext(String sql) {        
        return getSqlContext(sql).getExecuteContext();
    } 

    public SqlPrepareContext getPrepareContext(String sql) {        
        return getSqlContext(sql).getPrepareContext();
    }   
}
