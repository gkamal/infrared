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
import java.sql.SQLException;

import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Connection;
import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Factory;
import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;
import org.codehaus.aspectwerkz.AspectContext;
import org.codehaus.aspectwerkz.joinpoint.StaticJoinPoint;

/**
 * 
 * @author binil.thomas
 */
public class JdbcAspect {
    private InfraREDP6Factory factory;

    private Logger log = LoggingFactory.getLogger(JdbcAspect.class);

    public JdbcAspect(AspectContext actx) {
        factory = new InfraREDP6Factory();
    }

    public Object aroundGetConnection(StaticJoinPoint jp) throws Throwable {
        Connection con = (Connection) jp.proceed();
        if (!(con instanceof InfraREDP6Connection)) {            
            try {
                con = factory.getConnection(con);
                if (log.isDebugEnabled()) {
                    log.debug("Wrapped Connection with the Infrared P6 connection " + con);
                }
            } catch (SQLException sqlex) {
                log.error("Failed to wrap Connection with the Infrared P6 connection", sqlex);
            }
        }
        return con;
    }
}
