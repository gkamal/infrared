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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.StatisticsCollector;
import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.base.model.ExecutionTimer;

import com.p6spy.engine.spy.P6Connection;

/**
 * Wrapper over {@link com.p6spy.engine.spy.P6Connection}. Intercepts
 * com.p6spy.engine.spy.P6Connection methods for monitoring.
 * 
 * @author kamal.govindraj
 * @author prashant.nair
 * @author binil.thomas
 */
public class InfraREDP6Connection extends P6Connection {
    private InfraREDP6Factory factory;

    public InfraREDP6Connection(InfraREDP6Factory infraP6Factory, Connection conn)
            throws SQLException {
        super(infraP6Factory, conn);
        this.factory = infraP6Factory;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement pstmt = null;
        if (factory.isJDBCMonitoringEnabled()) {
            SqlPrepareContext ctx = getPrepareContext(sql);
            ExecutionTimer timer = new ExecutionTimer(ctx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {                
                pstmt = super.prepareStatement(sql);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            pstmt = super.prepareStatement(sql);
        }
        return pstmt;
    }

    public PreparedStatement prepareStatement(String sql, int p1, int p2) throws SQLException {
        PreparedStatement pstmt = null;
        if (factory.isJDBCMonitoringEnabled()) {
            SqlPrepareContext ctx = getPrepareContext(sql);
            ExecutionTimer timer = new ExecutionTimer(ctx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                pstmt = super.prepareStatement(sql, p1, p2);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            pstmt = super.prepareStatement(sql, p1, p2);
        }
        return pstmt;
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        CallableStatement cstmt = null;
        if (factory.isJDBCMonitoringEnabled()) {
            SqlPrepareContext ctx = getPrepareContext(sql);
            ExecutionTimer timer = new ExecutionTimer(ctx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                cstmt = super.prepareCall(sql);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            cstmt = super.prepareCall(sql);
        }

        return cstmt;
    }

    public CallableStatement prepareCall(String sql, int p1, int p2) throws SQLException {
        CallableStatement cstmt = null;
        if (factory.isJDBCMonitoringEnabled()) {
            SqlPrepareContext ctx = getPrepareContext(sql);
            ExecutionTimer timer = new ExecutionTimer(ctx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                cstmt = super.prepareCall(sql);
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            cstmt = super.prepareCall(sql);
        }
        return cstmt;
    }

    public void commit() throws SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            final String methodName = "commit";
            ApiContext ctx = new ApiContext(Connection.class.getName(), methodName, "JDBC");
            ExecutionTimer timer = new ExecutionTimer(ctx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                super.commit();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            super.commit();
        }
    }

    public void rollback() throws SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            final String methodName = "rollback";
            ApiContext ctx = new ApiContext(Connection.class.getName(), methodName, "JDBC");
            ExecutionTimer timer = new ExecutionTimer(ctx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                super.rollback();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            super.rollback();
        }
    }

    public void close() throws SQLException {
        if (factory.isJDBCMonitoringEnabled()) {
            final String methodName = "close";
            ApiContext ctx = new ApiContext(Connection.class.getName(), methodName, "JDBC");
            ExecutionTimer timer = new ExecutionTimer(ctx);
            StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
            try {
                super.close();
            } finally {
                MonitorFactory.getFacade().recordExecutionEnd(timer, col);
            }
        } else {
            super.close();
        }
    }
    
    private SqlPrepareContext getPrepareContext(String sql) {
        return factory.getPrepareContext(sql);
    }
}
