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
package net.sf.infrared.aspects;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.MonitorFacade;
import net.sf.infrared.agent.MonitorFactory;
import net.sf.infrared.agent.StatisticsCollector;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.ExecutionTimer;

import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

public abstract class AbstractBaseAspect {
    public Object recordExecution(ExecutionContext ctx, StaticJoinPoint sjp) throws Throwable {
        return recordExecution(ctx, sjp, MonitorFactory.getFacade());
    }
    
    public Object recordExecution(ExecutionContext ctx, 
            StaticJoinPoint sjp, MonitorFacade facade) throws Throwable {
        /*
        if (sjp == null) {
            throw new IllegalArgumentException("Cannot proceed with a null JoinPoint");
        }
        if (ctx == null) {
            return sjp.proceed();
        }*/
        
        // @TODO iajc trips over here; find out how to enable JDK 1.4 assertions in iajc
        // assert sjp != null: "Cannot proceed with a null JoinPoint";
        // assert ctx != null: "Cannot record timing for a null context";
        
        ExecutionTimer timer = new ExecutionTimer(ctx);
        StatisticsCollector collector = facade.recordExecutionBegin(timer);
        try {
            return sjp.proceed();
        } finally {
            facade.recordExecutionEnd(timer, collector);
        }
    }
    
    public boolean isMonitoringEnabled(){
        MonitorFacade facade = MonitorFactory.getFacade();
        return isMonitoringEnabled(facade);
    }
    
    public boolean isMonitoringEnabled(MonitorFacade facade){        
        MonitorConfig cfg = facade.getConfiguration();
        return cfg.isMonitoringEnabled();
    }
}
