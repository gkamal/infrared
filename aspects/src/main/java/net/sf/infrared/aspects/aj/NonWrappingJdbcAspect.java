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

package net.sf.infrared.aspects.aj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.StatisticsCollector;
import net.sf.infrared.aspects.api.ApiContext;
import net.sf.infrared.aspects.jdbc.SqlContextManager;
import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.aspects.jdbc.SqlMemory;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Factory;
import net.sf.infrared.base.model.ExecutionTimer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * An aspect implementation which intercepts JDBC calls without wrapping using
 * a P6Spy decorator.
 * This was implemented as a solution for bug# 1423202 (casts to driver-specific
 * interfaces fail).
 */
@Aspect
public class NonWrappingJdbcAspect {
    private static SqlContextManager ctxMgr = new SqlContextManager();

    private static SqlMemory sqlMem = new SqlMemory();

    @Pointcut("execution( public * java.sql.Statement+.execute*(String, ..) ) && args(sql, ..)")
    public void simpleSqlExecution(String sql){}
    
    @Pointcut("( execution( public java.sql.PreparedStatement java.sql.Connection+.prepareStatement(String, ..) )  " +
    		"|| execution( public java.sql.CallableStatement java.sql.Connection+.prepareCall(String, ..) ) )")
    public void statementOrCallPreparation(){}    
	
    @Pointcut(" ( execution( public * java.sql.PreparedStatement+.execute*() ) " +
    		"|| execution( public * java.sql.CallableStatement+.execute*() ) )  " +
    		"&& target(ps)")
    public void statementOrCallExecution(PreparedStatement ps){}
         
    
    @Pointcut("statementOrCallPreparation() && !cflowbelow(statementOrCallPreparation()) && args(sql)")
    public void firstStatementOrCallPreparation(String sql) {} 
         
    
    @Pointcut("execution( public void java.sql.Connection+.commit() )")
    public void commit(){}
              
    @Pointcut("execution( public void java.sql.Connection+.rollback() )")     
    public void rollback(){}
         
    @Pointcut("execution( public boolean java.sql.ResultSet+.next() )")
    public void iterateOverResultSet(){}
                                 
    
    @Around("simpleSqlExecution(sql)")
    public Object aroundSimpleSqlExecution(ProceedingJoinPoint proceedingJoinPoint,String sql) throws Throwable {      
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            return proceedingJoinPoint.proceed();
    	}
        SqlExecuteContext ctx = ctxMgr.getExecuteContext(sql);
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            facade.recordExecutionEnd(timer, collector);
        }        
    }

    @Around("firstStatementOrCallPreparation(sql)")
    public Object aroundFirstStatementOrCallPreparation(ProceedingJoinPoint proceedingJoinPoint,String sql) throws Throwable  {              
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            Object ps = proceedingJoinPoint.proceed();
            sqlMem.memorizeSql(sql, ps);
            return ps;
    	}
        
        SqlPrepareContext ctx = ctxMgr.getPrepareContext(sql);
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        Object ps = null;
        try {
            ps = proceedingJoinPoint.proceed();            
            return ps;
        } finally {
            facade.recordExecutionEnd(timer, collector);
            if (ps != null) {
                sqlMem.memorizeSql(sql, ps);
            }
        }  
    }

    @Around("statementOrCallExecution(ps)")
    public Object aroundStatementOrCallExecution(ProceedingJoinPoint proceedingJoinPoint,PreparedStatement ps) throws Throwable  {
        MonitorFacade facade = MonitorFactory.getFacade();
        if(! isJDBCMonitoringEnabled(facade) ) {
            return proceedingJoinPoint.proceed();
    	}

        String sql = sqlMem.recollectSql(ps);
        if (sql == null) {
            // we somehow missed recording the SQL; this is as good (or bad) as
            // having the JDBC monitoring turned off for this execution.
            return proceedingJoinPoint.proceed(); 
        }
        
        SqlExecuteContext ctx = ctxMgr.getExecuteContext(sql);
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        try {            
            return proceedingJoinPoint.proceed();
        } finally {
            facade.recordExecutionEnd(timer, collector);
        }
    }
    
    
    //
    // @TODO: The following advices commit, rollback and next all look the same - except for their
    // contexts; can these be folded into some common code??
    //
    @Around("commit()")
    public Object aroundCommit(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        MonitorFacade facade = MonitorFactory.getFacade();
		if(! isJDBCMonitoringEnabled(facade) ) {
            return proceedingJoinPoint.proceed();            
    	}
        
        ApiContext ctx = new ApiContext(Connection.class.getName(), "commit", "JDBC");
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);

        try {
            return proceedingJoinPoint.proceed();            
        } finally {
            facade.recordExecutionEnd(timer, collector);            
        }  
    }
    
    @Around("rollback()")
    public Object aroundRollback(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MonitorFacade facade = MonitorFactory.getFacade();
		if(! isJDBCMonitoringEnabled(facade) ) {
            return proceedingJoinPoint.proceed();            
    	}
        
        ApiContext ctx = new ApiContext(Connection.class.getName(), "rollback", "JDBC");
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);

        try {
            return proceedingJoinPoint.proceed();            
        } finally {
            facade.recordExecutionEnd(timer, collector);            
        }  
    }
    
    @Around("iterateOverResultSet()")
    public Object aroundIterateOverResultSet(ProceedingJoinPoint proceedingJoinPoint) throws Throwable  {
        MonitorFacade facade = MonitorFactory.getFacade();
		if(! isJDBCMonitoringEnabled(facade) ) {
            return proceedingJoinPoint.proceed();            
    	}
        
        ApiContext apiCtx = new ApiContext(ResultSet.class.getName(), "next", "JDBC");
        ExecutionTimer timer = new ExecutionTimer(apiCtx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);

        try {
            return proceedingJoinPoint.proceed();            
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