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

package net.sf.infrared.aspects.aj;

import java.sql.Connection;
import java.sql.SQLException;

import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Connection;
import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Factory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * An AspectJ aspect to wrap a java.sql.Connection with the InfraREDP6Connection
 */
@Aspect
public class JdbcAspect {

    private InfraREDP6Factory factory = new InfraREDP6Factory();
	
    @Pointcut("execution(public java.sql.Connection+ java.sql.Driver+.connect(..)) || execution(public java.sql.Connection+ javax.sql.DataSource+.getConnection(..))")
    public void getConnection() {}
        
    @Pointcut("getConnection()") // && !cflowbelow(getConnection())
    public void firstCallToGetConnection() {}

    // I would have loved to add a debug log here, but looks like AspectJ
    // and JarJar does not work together nicely. Need to look at this again.
    @Around("firstCallToGetConnection()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable  {
        Connection con = (Connection) joinPoint.proceed();
        if (!(con instanceof InfraREDP6Connection)) {
            try {                
                con = factory.getConnection(con);
            } catch (SQLException sqlex) {
            }
        }
        return con;	
    }
}

