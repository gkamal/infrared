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
 * Contributor(s):   prashant.nair, binil.thomas;
 *
 */
package net.sf.infrared.aspects.jdbc.p6spy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.StatisticsCollector;
import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.base.model.ExecutionTimer;

import com.p6spy.engine.spy.P6Connection;
import com.p6spy.engine.spy.P6Statement;

/**
 * Wrapper over {@link java.sql.Statement}. Intercepts java.sql.Statement
 * methods for monitoring.
 * 
 * @author kamal.govindraj
 * @author prashant.nair
 * @author binil.thomas
 */
public class InfraREDP6Statement implements Statement {
    protected InfraREDP6Factory factory;

    protected P6Connection conn;

    protected Statement passThru;

    protected String query;

    protected P6Statement p6Statement;

    public InfraREDP6Statement(InfraREDP6Factory infraP6Factory, Statement statement,
            P6Connection conn) {
        this.factory = infraP6Factory;
        this.conn = conn;
        passThru = statement;
    }

    public ResultSet executeQuery(String sql) throws java.sql.SQLException {
        ResultSet resultSet;
        query = sql;
        
        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getSqlContext(sql).getExecuteContext();
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {            
                resultSet = passThru.executeQuery(sql);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            resultSet = passThru.executeQuery(sql);
        }
        return factory.getResultSet(resultSet, getP6Statement(), query, query);
    }

    public int executeUpdate(String sql) throws java.sql.SQLException {
        query = sql;

        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getSqlContext(sql).getExecuteContext();
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {            
                return passThru.executeUpdate(sql);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.executeUpdate(sql);
        }
    }

    public void addBatch(String sql) throws java.sql.SQLException {
        query += "; " + sql;
        passThru.addBatch(sql);
    }

    public int[] executeBatch() throws java.sql.SQLException {
        if(query == null) {
            return passThru.executeBatch(); 
        }
        if (factory.isJDBCMonitoringEnabled()) {        
            SqlExecuteContext executeCtx = factory.getSqlContext(query).getExecuteContext();
            ExecutionTimer timer = new ExecutionTimer(executeCtx);
        
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {            
                return passThru.executeBatch();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.executeBatch(); 
        }
    }

    public int executeUpdate(String sql, int p1) throws java.sql.SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getSqlContext(sql).getExecuteContext();
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                return passThru.executeUpdate(sql, p1);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.executeUpdate(sql, p1);
        }
    }

    public int executeUpdate(String sql, int p1[]) throws java.sql.SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getSqlContext(sql).getExecuteContext();
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {            
                return passThru.executeUpdate(sql, p1);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.executeUpdate(sql, p1);
        }
    }

    public int executeUpdate(String sql, String p1[]) throws java.sql.SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getSqlContext(sql).getExecuteContext();
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                return passThru.executeUpdate(sql, p1);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.executeUpdate(sql, p1);
        }
    }

    public boolean execute(String sql, int p1) throws java.sql.SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getExecuteContext(sql);
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                return passThru.execute(sql, p1);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.execute(sql, p1);
        }
    }

    public boolean execute(String sql, int p1[]) throws java.sql.SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getExecuteContext(sql);
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                return passThru.execute(sql, p1);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.execute(sql, p1);
        }
    }

    public boolean execute(String sql, String p1[]) throws java.sql.SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getExecuteContext(sql);
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                return passThru.execute(sql, p1);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.execute(sql, p1);
        }
    }

    public void close() throws SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            final String methodName = "close";
            ApiContext ctx = new ApiContext(Statement.class.getName(), methodName, "JDBC");
            ExecutionTimer timer = new ExecutionTimer(ctx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                passThru.close();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            passThru.close();
        }
    }

    public int getMaxFieldSize() throws SQLException {
        return passThru.getMaxFieldSize();
    }

    public void setMaxFieldSize(int max) throws SQLException {
        passThru.setMaxFieldSize(max);
    }

    public int getMaxRows() throws SQLException {
        return passThru.getMaxRows();
    }

    public void setMaxRows(int max) throws SQLException {
        passThru.setMaxRows(max);
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        passThru.setEscapeProcessing(enable);
    }

    public int getQueryTimeout() throws SQLException {
        return passThru.getQueryTimeout();
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        passThru.setQueryTimeout(seconds);
    }

    public void cancel() throws SQLException {
        passThru.cancel();
    }

    public SQLWarning getWarnings() throws SQLException {
        return passThru.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        passThru.clearWarnings();
    }

    public void setCursorName(String name) throws SQLException {
        passThru.setCursorName(name);
    }

    public boolean execute(String sql) throws SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            SqlExecuteContext executeCtx = factory.getExecuteContext(sql);
            ExecutionTimer timer = new ExecutionTimer(executeCtx);

            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                return passThru.execute(sql);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            return passThru.execute(sql);
        }
    }

    public ResultSet getResultSet() throws SQLException {
        return factory.getResultSet(passThru.getResultSet(), getP6Statement(), query, query);
    }

    public int getUpdateCount() throws SQLException {
        return passThru.getUpdateCount();
    }

    public boolean getMoreResults() throws SQLException {
        return passThru.getMoreResults();
    }

    public void setFetchDirection(int direction) throws SQLException {
        passThru.setFetchDirection(direction);
    }

    public int getFetchDirection() throws SQLException {
        return passThru.getFetchDirection();
    }

    public void setFetchSize(int rows) throws SQLException {
        passThru.setFetchSize(rows);
    }

    public int getFetchSize() throws SQLException {
        return passThru.getFetchSize();
    }

    public int getResultSetConcurrency() throws SQLException {
        return passThru.getResultSetConcurrency();
    }

    public int getResultSetType() throws SQLException {
        return passThru.getResultSetType();
    }

    public void clearBatch() throws SQLException {
        passThru.clearBatch();
    }

    public Connection getConnection() throws SQLException {
        return conn;
    }

    public boolean getMoreResults(int current) throws SQLException {
        return passThru.getMoreResults(current);
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        return factory.getResultSet(passThru.getGeneratedKeys(), 
                new P6Statement(factory, this, conn), "", query);
    }

    public int getResultSetHoldability() throws SQLException {
        return passThru.getResultSetHoldability();
    }

    protected P6Statement getP6Statement() {
        if (p6Statement == null) {
            p6Statement = new P6Statement(factory, this, conn);
        }
        return p6Statement;
    }

//	public boolean isClosed() throws SQLException {
//		return passThru.isClosed();
//	}
//
//	public boolean isPoolable() throws SQLException {
//		return passThru.isPoolable();
//	}
//
//	public void setPoolable(boolean poolable) throws SQLException {
//		passThru.setPoolable(poolable);
//	}
//
//	public boolean isWrapperFor(Class arg0) throws SQLException {
//		return passThru.isWrapperFor(arg0);
//	}
//
//	public Object unwrap(Class arg0) throws SQLException {
//		return passThru.unwrap(arg0);
//	}
}
