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
import java.sql.Driver;
import javax.sql.DataSource;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Connection;
import net.sf.infrared.aspects.jdbc.p6spy.InfraREDP6Factory;
import net.sf.infrared.base.util.LoggingFactory;

/**
 * An AspectJ aspect to wrap a java.sql.Connection with the InfraREDP6Connection
 */
public aspect JdbcAspect {

    private InfraREDP6Factory factory = new InfraREDP6Factory();
	
    public pointcut getConnection() : execution(public Connection+ Driver+.connect(..)) || 
        execution(public Connection+ DataSource+.getConnection(..));
        
    public pointcut firstCallToGetConnection() : getConnection() && !cflowbelow(getConnection());

    // I would have loved to add a debug log here, but looks like AspectJ
    // and JarJar does not work together nicely. Need to look at this again.
    Object around() : firstCallToGetConnection() {
        Connection con = (Connection) proceed();
        if (!(con instanceof InfraREDP6Connection)) {
            try {                
                con = factory.getConnection(con);
            } catch (SQLException sqlex) {
            }
        }
        return con;	
    }
}
