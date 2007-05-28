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

import java.util.List;
import java.util.TimerTask;

import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.collector.StatisticsRepository;

import org.apache.log4j.Logger;

public class PersistTask extends TimerTask {
    private static final Logger log = LoggingFactory.getLogger(PersistTask.class);
    
    private StatisticsRepository statsRepository;

    private SpringContext ctx;

    public PersistTask(StatisticsRepository statsRepository) {
        this.statsRepository = statsRepository;
        this.ctx = new SpringContext();
    }

    public void run() {
        List statsListToPersist = statsRepository.getStatisticsToPersist();
        ApplicationStatistics[] statsArrayToPersist = (ApplicationStatistics[]) statsListToPersist
                .toArray(new ApplicationStatistics[] {});
        ctx.getDao().saveStatistics(statsArrayToPersist);
        
        statsRepository.clearStatsSinceLastPersist();
        if (log.isDebugEnabled()) {
            log.debug("Persisted " + statsArrayToPersist.length + " statistics");
        }
    }
    public void shutdown() {
        if(ctx != null) {
            ctx.shutdown();
        }
    }
}
