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
 * Original Author:  subin.p (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.collector;

import java.util.Collection;
import java.util.Date;

import javax.sql.DataSource;

import net.sf.infrared.base.model.ApplicationStatistics;
import net.sf.infrared.base.model.StatisticsSnapshot;

public interface ApplicationStatisticsDao {
    void saveStatistics(ApplicationStatistics stats);

    void saveStatistics(ApplicationStatistics[] stats);

    StatisticsSnapshot fetchStatistics(Collection appNames,
            Collection instanceIds, Date from, Date to);


    DataSource getDaoDataSource();
}
