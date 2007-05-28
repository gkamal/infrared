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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.StatisticsCollector;
import net.sf.infrared.aspects.AbstractBaseAspect;
import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Factory;
import net.sf.infrared.base.model.ExecutionTimer;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

/**
 * An aspect implementation which intercepts JDBC calls without wrapping using
 * a P6Spy decorator.
 * This was implemented as a solution for bug# 1423202 (casts to driver-specific
 * interfaces fail).
 *
 * @author binil.thomas
 */
public class NonWrappingJdbcAspect extends AbstractBaseAspect {
    private static SqlContextManager ctxMgr = new SqlContextManager();

    private static SqlMemory sqlMem = new SqlMemory();
    
    public Object aroundSimpleSqlExecution(StaticJoinPoint sjp, String sql) throws Throwable {      
        MonitorFacade facade = MonitorFactory.getFacade();
    	if(! isJDBCMonitoringEnabled(facade) ) {
            return sjp.proceed();
    	}
        SqlExecuteContext ctx = ctxMgr.getExecuteContext(sql);
        return recordExecution(ctx, sjp, facade);    
    }

    public Object aroundFirstStatementOrCallPreparation(StaticJoinPoint sjp, String sql) throws Throwable {              
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            Object ps = sjp.proceed();
            sqlMem.memorizeSql(sql, ps);
            return ps;
    	}
        
        SqlPrepareContext ctx = ctxMgr.getPrepareContext(sql);
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        Object ps = null;
        try {
            ps = sjp.proceed();            
            return ps;
        } finally {
            facade.recordExecutionEnd(timer, collector);
            if (ps != null) {
                sqlMem.memorizeSql(sql, ps);
            }
        }  
    }

    public Object aroundStatementOrCallExecution(StaticJoinPoint sjp, PreparedStatement ps) throws Throwable {
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            return sjp.proceed();
    	}

        String sql = sqlMem.recollectSql(ps);
        if (sql == null) {
            // we somehow missed recording the SQL; this is as good (or bad) as
            // having the JDBC monitoring turned off for this execution.
            return sjp.proceed(); 
        }
        
        SqlExecuteContext ctx = ctxMgr.getExecuteContext(sql);
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        try {            
            return sjp.proceed();
        } finally {
            facade.recordExecutionEnd(timer, collector);
        }
    }
    
    public Object aroundCommit(StaticJoinPoint sjp) throws Throwable {        
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            return sjp.proceed();            
    	}
        
        ApiContext ctx = new ApiContext(Connection.class.getName(), "commit", "JDBC");
        return recordExecution(ctx, sjp, facade);  
    }
    
    public Object aroundRollback(StaticJoinPoint sjp) throws Throwable {        
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            return sjp.proceed();            
    	}
        
        ApiContext ctx = new ApiContext(Connection.class.getName(), "rollback", "JDBC");
        return recordExecution(ctx, sjp, facade);  
    }
    
    public Object aroundIterateOverResultSet(StaticJoinPoint sjp) throws Throwable {
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            return sjp.proceed();            
    	}
        
        ApiContext apiCtx = new ApiContext(ResultSet.class.getName(), "next", "JDBC");
        return recordExecution(apiCtx, sjp, facade);    
    }
    
    boolean isJDBCMonitoringEnabled(MonitorFacade facade) {
        MonitorConfig cfg = facade.getConfiguration();
        return cfg.isMonitoringEnabled() 
            && cfg.getProperty(InfraREDP6Factory.KEY_JDBC_MONITORING_ENABLED, true);
    }
}
