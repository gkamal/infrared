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
 * Contributor(s):   subin.p;
 *
 */
package net.sf.infrared.collector.impl.persistence;

import net.sf.infrared.collector.ApplicationStatisticsDao;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {

    private static final String daoBeanName = "infraredDao";

    private ClassPathXmlApplicationContext ctx;

    public SpringContext() {
        initialize();
    }

    public void initialize() {
        this.ctx = new ClassPathXmlApplicationContext(
                "net/sf/infrared/collector/impl/persistence/infrared-spring.xml");
    }

    public ApplicationStatisticsDao getDao() {
        return (ApplicationStatisticsDao) ctx.getBean(daoBeanName);
    }

    public void shutdown() {
        this.ctx.close();
    }
    
    //added for testing purpose. Need to figure out if this is this ok ?
    ClassPathXmlApplicationContext getContext() {
    	return ctx;
    }
}
