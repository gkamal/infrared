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
import net.sf.infrared.aspects.hibernate.HibernateQueryContext;
import net.sf.infrared.base.model.ExecutionTimer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
 
@Aspect 
public class Hibernate3AjAspect{
 	
	@Pointcut("execution(public java.util.List org.hibernate.Query+.list()) && target(query)")
	public void hib3Query(org.hibernate.Query query){} 
 				
 	
	@Pointcut("execution(public * org.hibernate.Session+.find()) && args(queryString, ..)")
 	public void hib3Find(String queryString){} 
 					
	@Around("hib3Query(query)")
	public Object around(ProceedingJoinPoint joinPoint, org.hibernate.Query query) throws Throwable {
  		MonitorFacade facade = getFacade();
  		
  		if(isMonitoringEnabled(facade)){
  			HibernateQueryContext ctx = new HibernateQueryContext(query.toString());
	        ExecutionTimer timer = new ExecutionTimer(ctx);
        	StatisticsCollector collector = facade.recordExecutionBegin(timer);
        	try {
            	return joinPoint.proceed();
        	} 
        	finally {
            	facade.recordExecutionEnd(timer, collector);
        	}  			
  		}
  		else{
  			return joinPoint.proceed();
  		}		
	}
	
	@Around("hib3Find(queryString)")
	public Object around(ProceedingJoinPoint joinPoint, String queryString) throws Throwable  {
  		MonitorFacade facade = getFacade();
  		
  		if(isMonitoringEnabled(facade)){
  			HibernateQueryContext ctx = new HibernateQueryContext(queryString);
	        ExecutionTimer timer = new ExecutionTimer(ctx);
        	StatisticsCollector collector = facade.recordExecutionBegin(timer);
        	try {
            	return joinPoint.proceed();
        	} 
        	finally {
            	facade.recordExecutionEnd(timer, collector);
        	}  			
  		}
  		else{
  			return joinPoint.proceed();
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