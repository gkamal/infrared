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

package net.sf.infrared.aspects.aj;

import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.StatisticsCollector;
import net.sf.infrared.aspects.jdbc.SqlContext;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Factory;
import net.sf.infrared.aspects.jdbc.SqlContextManager;
import net.sf.infrared.aspects.jdbc.SqlMemory;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * An aspect implementation which intercepts JDBC calls without wrapping using
 * a P6Spy decorator.
 * This was implemented as a solution for bug# 1423202 (casts to driver-specific
 * interfaces fail).
 */
public aspect NonWrappingJdbcAspect {
    private static SqlContextManager ctxMgr = new SqlContextManager();

    private static SqlMemory sqlMem = new SqlMemory();

    pointcut simpleSqlExecution(String sql):
        execution( public * Statement+.execute*(String, ..) ) && args(sql, ..);
    
    pointcut statementOrCallPreparation():
        ( execution( public PreparedStatement Connection+.prepareStatement(String, ..) ) 
       || execution( public CallableStatement Connection+.prepareCall(String, ..) ) );    
									    									
    pointcut statementOrCallExecution(PreparedStatement ps):
          ( execution( public * PreparedStatement+.execute*() ) 
         || execution( public * CallableStatement+.execute*() ) )
       && target(ps);
    
    public pointcut firstStatementOrCallPreparation(String sql) : 
         statementOrCallPreparation() && !cflowbelow(statementOrCallPreparation()) && args(sql);
         
    public pointcut commit():
         execution( public void Connection+.commit() );     
         
    public pointcut rollback():
         execution( public void Connection+.rollback() );
         
    public pointcut iterateOverResultSet():
         execution( public boolean ResultSet+.next() );                        
    		
    Object around(String sql) : simpleSqlExecution(sql) {      
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            return proceed(sql);
    	}
        SqlExecuteContext ctx = ctxMgr.getExecuteContext(sql);
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        try {
            return proceed(sql);
        } finally {
            facade.recordExecutionEnd(timer, collector);
        }        
    }

    Object around(String sql) : firstStatementOrCallPreparation(sql) {              
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            Object ps = proceed(sql);
            sqlMem.memorizeSql(sql, ps);
            return ps;
    	}
        
        SqlPrepareContext ctx = ctxMgr.getPrepareContext(sql);
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        Object ps = null;
        try {
            ps = proceed(sql);            
            return ps;
        } finally {
            facade.recordExecutionEnd(timer, collector);
            if (ps != null) {
                sqlMem.memorizeSql(sql, ps);
            }
        }  
    }

    Object around(PreparedStatement ps) : statementOrCallExecution(ps) {
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            return proceed(ps);
    	}

        String sql = sqlMem.recollectSql(ps);
        if (sql == null) {
            // we somehow missed recording the SQL; this is as good (or bad) as
            // having the JDBC monitoring turned off for this execution.
            return proceed(ps); 
        }
        
        SqlExecuteContext ctx = ctxMgr.getExecuteContext(sql);
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        try {            
            return proceed(ps);
        } finally {
            facade.recordExecutionEnd(timer, collector);
        }
    }
    
    
    //
    // @TODO: The following advices commit, rollback and next all look the same - except for their
    // contexts; can these be folded into some common code??
    //
    Object around(): commit() {
        MonitorFacade facade = MonitorFactory.getFacade();
		if(! isJDBCMonitoringEnabled(facade) ) {
            return proceed();            
    	}
        
        ApiContext ctx = new ApiContext(Connection.class.getName(), "commit", "JDBC");
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);

        try {
            return proceed();            
        } finally {
            facade.recordExecutionEnd(timer, collector);            
        }  
    }
    
    Object around(): rollback() {
        MonitorFacade facade = MonitorFactory.getFacade();
		if(! isJDBCMonitoringEnabled(facade) ) {
            return proceed();            
    	}
        
        ApiContext ctx = new ApiContext(Connection.class.getName(), "rollback", "JDBC");
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);

        try {
            return proceed();            
        } finally {
            facade.recordExecutionEnd(timer, collector);            
        }  
    }
    
    Object around(): iterateOverResultSet() {
        MonitorFacade facade = MonitorFactory.getFacade();
		if(! isJDBCMonitoringEnabled(facade) ) {
            return proceed();            
    	}
        
        ApiContext apiCtx = new ApiContext(ResultSet.class.getName(), "next", "JDBC");
        ExecutionTimer timer = new ExecutionTimer(apiCtx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);

        try {
            return proceed();            
        } finally {
            facade.recordExecutionEnd(timer, collector);            
        }  
    }
    
    boolean isJDBCMonitoringEnabled(MonitorFacade facade) {
        MonitorConfig cfg = facade.getConfiguration();
        return cfg.isMonitoringEnabled() 
            && cfg.getProperty(InfraREDP6Factory.KEY_JDBC_MONITORING_ENABLED, true);
    }
}