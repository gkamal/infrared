package net.sf.infrared.aspects.jdbc.p6spy;

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
 * Original Author:  chetan.mehrotra (Tavant Technologies)
 *
 */


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.agent.StatisticsCollector;

import com.p6spy.engine.spy.P6Connection;
import com.p6spy.engine.spy.P6PreparedStatement;
import com.p6spy.engine.spy.P6Statement;

/**
 * @author chetan.mehrotra
 * @date Dec 15, 2005
 * @version $Revision: 1.1 $ 
 */
public class InfraREDP6PreparedStatementWithVariables extends P6PreparedStatement { 
    protected InfraREDP6Factory factory;
	
    private ExecutionContext executeCtx;
	
    public InfraREDP6PreparedStatementWithVariables(InfraREDP6Factory infraP6Factory, 
            PreparedStatement real, P6Connection conn, String sql) {
	super(infraP6Factory, real, conn,sql);
	this.factory = infraP6Factory;		
    }
	
	
/*    public void addBatch() throws SQLException {
        statementQuery = getQueryFromPreparedStatement();
        SqlExecuteContext executeCtx = factory.getSqlContext(
         		getQueryFromPreparedStatement()).getExecuteContext();
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        try {
            prepStmtPassthru.addBatch();
        }
        finally {
        	MonitorFactory.getFacade().recordExecutionEnd(timer);
        }
    } */
    
    public boolean execute() throws SQLException {
        SqlExecuteContext executeCtx = factory.getExecuteContext(getQueryFromPreparedStatement());
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer); 
        
        try {
            return prepStmtPassthru.execute();
        } finally {
        	    MonitorFactory.getFacade().recordExecutionEnd(timer, col);
        }
    }
    
    public ResultSet executeQuery() throws SQLException {
    	ResultSet resultSet = null; 
    	SqlExecuteContext executeCtx = factory.getExecuteContext(getQueryFromPreparedStatement());
         ExecutionTimer timer = new ExecutionTimer(executeCtx);         
         StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
         try {             
             resultSet = prepStmtPassthru.executeQuery();
         } finally {
             MonitorFactory.getFacade().recordExecutionEnd(timer, col);
         }
         return factory.getResultSet(resultSet, getP6Statement(), preparedQuery,  getQueryFromPreparedStatement());
    }
    
    public int executeUpdate() throws SQLException {
    	int returnValue;
        SqlExecuteContext executeCtx = factory.getExecuteContext(getQueryFromPreparedStatement());
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
        try {            
            returnValue = prepStmtPassthru.executeUpdate();
        } finally {
            MonitorFactory.getFacade().recordExecutionEnd(timer, col);
        }
        return returnValue;
    }
    
    // ---------------------------------------------------------------------------------------
    // we need to override the same methods that P6SLogStatement overrides because we don't have
    // multiple inheritance.  considering the alternatives (delegation), it seems cleaner
    // to just override the methods.  to understand why this is true, realize
    // P6LogPreparedStatement inherits from P6PreparedStatement which inherits from P6Statement,
    // so P6LogPreparedStatement never inherits from P6LogStatement and therefore it does not
    // inherit any of the functionality we define in P6LogStatement.
    // ---------------------------------------------------------------------------------------
    
    public boolean execute(String p0) throws java.sql.SQLException {
    	 statementQuery = p0;
         SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
         ExecutionTimer timer = new ExecutionTimer(executeCtx);
         StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
         try {             
             return passthru.execute(p0);
         } finally {
             MonitorFactory.getFacade().recordExecutionEnd(timer, col);
         }
    }
    
    // Since JDK 1.4
    public boolean execute(String p0, int p1) throws java.sql.SQLException {
        statementQuery = p0;
        SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
        try {            
            return passthru.execute(p0, p1);
        } finally {
            MonitorFactory.getFacade().recordExecutionEnd(timer, col);
        }
    }
    
    // Since JDK 1.4
    public boolean execute(String p0, int p1[]) throws java.sql.SQLException {
    	statementQuery = p0;
    	 SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
         ExecutionTimer timer = new ExecutionTimer(executeCtx);
         StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
         try {             
             return passthru.execute(p0, p1);
         } finally {
             MonitorFactory.getFacade().recordExecutionEnd(timer, col);
         }
    }
    
    // Since JDK 1.4
    public boolean execute(String p0, String p1[]) throws java.sql.SQLException {
        statementQuery = p0;
        SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
        try {        	
            return passthru.execute(p0, p1);
        }
        finally {
        	 MonitorFactory.getFacade().recordExecutionEnd(timer, col);
        }
    }
    
    public ResultSet executeQuery(String p0) throws java.sql.SQLException {
    	ResultSet resultSet;
    	statementQuery = p0;
        SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);          
          try {              
              resultSet = passthru.executeQuery(p0);
          } finally {
              MonitorFactory.getFacade().recordExecutionEnd(timer, col);
          }
          return factory.getResultSet(resultSet, getP6Statement(), p0, p0);
    }
    
    public int executeUpdate(String p0) throws java.sql.SQLException {
        statementQuery = p0;
        SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
        try {            
            return passthru.executeUpdate(p0);
        } finally {
            MonitorFactory.getFacade().recordExecutionEnd(timer, col);
        }
    }
    
    // Since JDK 1.4
    public int executeUpdate(String p0, int p1) throws java.sql.SQLException {
        statementQuery = p0;
        SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
        try {            
            return passthru.executeUpdate(p0, p1);
        } finally {
            MonitorFactory.getFacade().recordExecutionEnd(timer, col);
        }
    }
    
    // Since JDK 1.4
    public int executeUpdate(String p0, int p1[]) throws java.sql.SQLException {
        statementQuery = p0;
        SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
        ExecutionTimer timer = new ExecutionTimer(executeCtx);
        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
        try {        	
            return(passthru.executeUpdate(p0, p1));
        }
        finally {
        	 MonitorFactory.getFacade().recordExecutionEnd(timer, col);
        }
    }
    
    // Since JDK 1.4
    public int executeUpdate(String p0, String p1[]) throws java.sql.SQLException {
    	  int returnValue;
    	  statementQuery = p0;
    	  SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
          ExecutionTimer timer = new ExecutionTimer(executeCtx);
          StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
          try {              
              returnValue = passthru.executeUpdate(p0,p1);
          } finally {
              MonitorFactory.getFacade().recordExecutionEnd(timer, col);
          }
          return returnValue;
    }
    
//    public void addBatch(String p0) throws java.sql.SQLException {
//        statementQuery = p0;
//        SqlExecuteContext executeCtx = factory.getSqlContext(statementQuery).getExecuteContext();
//        ExecutionTimer timer = new ExecutionTimer(executeCtx);
//        StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
//        try {        	 
//            passthru.addBatch(p0);
//        }
//        finally {
//        	MonitorFactory.getFacade().recordExecutionEnd(timer, col);
//        }
//    }
    
    public int[] executeBatch() throws java.sql.SQLException {
    	 if(statementQuery == null) {
             return passthru.executeBatch(); 
         }
         SqlExecuteContext executeCtx = factory.getExecuteContext(statementQuery);
         ExecutionTimer timer = new ExecutionTimer(executeCtx);
         StatisticsCollector col = MonitorFactory.getFacade().recordExecutionBegin(timer);
        try {        	 
            return(passthru.executeBatch());
        }
        finally {
        	MonitorFactory.getFacade().recordExecutionEnd(timer, col);
        }
    }
	
    protected P6Statement getP6Statement() {
        return this;
    }
}

