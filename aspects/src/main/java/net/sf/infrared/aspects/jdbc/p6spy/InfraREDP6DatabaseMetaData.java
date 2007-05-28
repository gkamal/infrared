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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 * 
 */
package net.sf.infrared.aspects.jdbc.p6spy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import com.p6spy.engine.spy.P6Connection;
import com.p6spy.engine.spy.P6DatabaseMetaData;
import com.p6spy.engine.spy.P6Factory;

/**
 * 
 * @author prashant.nair
 */
public class InfraREDP6DatabaseMetaData extends P6DatabaseMetaData {
    private Connection underlyingConnection = null;

    public InfraREDP6DatabaseMetaData(P6Factory factory, DatabaseMetaData dbMetaData,
            P6Connection connection) {
        super(factory, dbMetaData, connection);
        underlyingConnection = connection.getJDBC();
    }

    /**
     * Return the underlying JDBC connection. A hook to get hold of the
     * underlying connection if required.
     */
    public Connection getConnection() throws SQLException {
        return underlyingConnection;
    }
}
