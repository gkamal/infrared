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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.aspects.jdbc.p6spy;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.aspects.jdbc.SqlContext;
import net.sf.infrared.aspects.jdbc.SqlContextManager;
import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

import com.p6spy.engine.spy.P6Connection;
import com.p6spy.engine.spy.P6CoreFactory;
import com.p6spy.engine.spy.P6Statement;



/**
 * Provide an implementation of the Factory class to plug in our wrapper class
 * implementation inplace of P6Spy. For the classes in which we are not
 * interested return the real JDBC class instead of the P6 wrapper. This is to
 * minimize the performance overhead.
 *
 * @author kamal.govindraj
 * @author binil.thomas
 */
public class InfraREDP6Factory extends P6CoreFactory {

    private static final Logger log = LoggingFactory.getLogger(InfraREDP6Factory.class);

    public static final String KEY_JDBC_MONITORING_ENABLED = "jdbc-monitoring-enable";

    public static final String KEY_JDBC_FETCH_STATISTICS_ENABLE = "jdbc-fetch-statistics";

	public static final String KEY_PREPARED_STATEMENT_MONITORING_ENABLED = "prepared-statement-monitoring-enable";

    private SqlContextManager ctxMgr = new SqlContextManager();

    static{
		log.debug("InfraREDP6Factory class is being used to wrap the database connection");
	}

    public Connection getConnection(Connection conn) throws SQLException {

        log.debug("InfraRED returning the wrapped InfraREDP6Connection");
        return new InfraREDP6Connection(this, conn);
    }

    public PreparedStatement getPreparedStatement(PreparedStatement real, P6Connection conn,
            String sql) throws SQLException {
        if (isJDBCMonitoringEnabled()) {
        	if (isPreparedStatementMonitoringEnabled()){
	            return new InfraREDP6PreparedStatementWithVariables(this, real, conn, sql);
			 } else {
				 return new InfraREDP6PreparedStatement(this, real, conn, sql);
			 }
        } else {
            return real;
        }
    }

    public Statement getStatement(Statement statement, P6Connection conn) throws SQLException {
        if (isJDBCMonitoringEnabled()) {
            return new InfraREDP6Statement(this, statement, conn);
        } else {
            return statement;
        }
    }

    public ResultSet getResultSet(ResultSet real, P6Statement statement, String preparedQuery,
            String query) throws SQLException {
        if (isJDBCMonitoringEnabled() && isCollectFetchDataEnabled()) {
            return (real == null) ? null : new InfraREDP6ResultSet(this, real, statement,
                    preparedQuery, query);
        } else {
            return real;
        }
    }

    public Array getArray(Array real, P6Statement statement, String preparedQuery, String query)
            throws SQLException {
        return real;
    }

    public ResultSetMetaData getResultSetMetaData(ResultSetMetaData real) throws SQLException {
        return real;
    }

    public CallableStatement getCallableStatement(CallableStatement real, P6Connection conn,
            String p0) throws SQLException {
        if (isJDBCMonitoringEnabled()) {
            return new InfraREDP6CallableStatement(this, real, conn, p0);
        } else {
            return real;
        }
    }

    public DatabaseMetaData getDatabaseMetaData(DatabaseMetaData real, P6Connection conn)
            throws SQLException {
        return new InfraREDP6DatabaseMetaData(this, real, conn);
    }

    public SqlContext getSqlContext(String sql) {
        return ctxMgr.getSqlContext(sql);
    }

    public SqlExecuteContext getExecuteContext(String sql) {
        return ctxMgr.getExecuteContext(sql);
    }

    public SqlPrepareContext getPrepareContext(String sql) {
        return ctxMgr.getPrepareContext(sql);
    }

    boolean isJDBCMonitoringEnabled() {
        MonitorConfig cfg = MonitorFactory.getFacade().getConfiguration();
        return cfg.isMonitoringEnabled() && cfg.getProperty(KEY_JDBC_MONITORING_ENABLED, true);
    }

    boolean isCollectFetchDataEnabled() {
        MonitorConfig cfg = MonitorFactory.getFacade().getConfiguration();
        return cfg.isMonitoringEnabled() && cfg.getProperty(KEY_JDBC_FETCH_STATISTICS_ENABLE, true);
    }

    boolean isPreparedStatementMonitoringEnabled() {
        MonitorConfig cfg = MonitorFactory.getFacade().getConfiguration();
        return cfg.isMonitoringEnabled() && cfg.getProperty(KEY_PREPARED_STATEMENT_MONITORING_ENABLED, false);
    }
}
