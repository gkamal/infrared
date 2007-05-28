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

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.StatisticsCollector;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.aspects.hibernate.HibernateQueryContext;
 
public aspect Hibernate2AjAspect{
 	
	public pointcut hib2Query(net.sf.hibernate.Query query) : 
 				execution(public java.util.List net.sf.hibernate.Query+.list()) && target(query);
 	
 	public pointcut hib2Find(String queryString) : 
 				execution(public * net.sf.hibernate.Session+.find(..)) && args(queryString, ..);	
 				
	Object around(net.sf.hibernate.Query query) : hib2Query(query){
  		MonitorFacade facade = getFacade();
  		
  		if(isMonitoringEnabled(facade)){
  			HibernateQueryContext ctx = new HibernateQueryContext(query.toString());
	        ExecutionTimer timer = new ExecutionTimer(ctx);
        	StatisticsCollector collector = facade.recordExecutionBegin(timer);
        	try {
            	return proceed(query);
        	} 
        	finally {
            	facade.recordExecutionEnd(timer, collector);
        	}  			
  		}
  		else{
  			return proceed(query);
  		}		
	}
	
	Object around(String queryString) : hib2Find(queryString){
  		MonitorFacade facade = getFacade();
  		
  		if(isMonitoringEnabled(facade)){
  			HibernateQueryContext ctx = new HibernateQueryContext(queryString);
	        ExecutionTimer timer = new ExecutionTimer(ctx);
        	StatisticsCollector collector = facade.recordExecutionBegin(timer);
        	try {
            	return proceed(queryString);
        	} 
        	finally {
            	facade.recordExecutionEnd(timer, collector);
        	}  			
  		}
  		else{
  			return proceed(queryString);
  		}
	}
	
  	public MonitorFacade getFacade(){
        return MonitorFactory.getFacade();
    }    
    
    public boolean isMonitoringEnabled(MonitorFacade facade){        
        MonitorConfig cfg = facade.getConfiguration();
        return cfg.isMonitoringEnabled();
    }	 						 				 	
}