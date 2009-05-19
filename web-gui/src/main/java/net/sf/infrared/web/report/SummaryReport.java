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
package net.sf.infrared.web.report;

import java.io.IOException;
import java.io.OutputStream;

import net.sf.infrared.web.util.PerformanceDataSnapshot;

/**
 * Defines interfaces that handle Summary Report. Implementations may decide upon presentation of the summary report.
 */
public interface SummaryReport
{
    /**
     * Add the snapshot to the report
     * @param snapShot
     */
    void addSnapShot(PerformanceDataSnapshot snapShot);

    /**
     * Save the report
     * @param fileName - file into which report needs to be saved.
     * @throws IOException
     */
    void save(String fileName) throws IOException;

    /**
     * Save the report to a stream
     * @param os - output stream to which the report needs to be saved.
     * @throws IOException
     */
    void save(OutputStream os) throws IOException;
}
