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
 * Contributor(s):   subin.p; 
 *
 */
package net.sf.infrared.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.infrared.web.report.ReportFactory;
import net.sf.infrared.web.report.SummaryReport;
import net.sf.infrared.web.util.PerformanceDataSnapshot;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This is the action class, which generates an excel report of the performance statistics.
 */
public class ExportSummaryReportToExcel
        extends Action
{
     public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException
     {
        HttpSession session = request.getSession();         
        SummaryReport report = ReportFactory.createReport(ReportFactory.EXCEL);
        
        PerformanceDataSnapshot perfData = 
        								(PerformanceDataSnapshot) session.getAttribute("perfData");
        
        report.addSnapShot(perfData);
        
        response.setContentType("application/vnd.ms-excel");
        report.save(response.getOutputStream());
        return null;
    }
}
