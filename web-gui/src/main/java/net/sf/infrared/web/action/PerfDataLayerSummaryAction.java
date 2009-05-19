/*
 *
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
 * Original Author:  kaushal.kumar (Tavant Technologies)
 * Contributor(s):   prashant.nair;
 *
 */
package net.sf.infrared.web.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.web.formbean.AggrExecTimeFormBean;
import net.sf.infrared.web.formbean.SqlStatisticsFormBean;
import net.sf.infrared.web.util.PerformanceDataSnapshot;
import net.sf.infrared.web.util.SqlStatistics;
import net.sf.infrared.web.util.ViewUtil;
import net.sf.infrared.web.util.WebConfig;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PerfDataLayerSummaryAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response) 
                                    throws Exception {
        HttpSession session = request.getSession();
        String apiType = request.getParameter("type");
        String layerType = request.getParameter("layerType");
        if(layerType == null){
            layerType = (String)session.getAttribute("layerType");
        }
        else{
            session.setAttribute("layerType", layerType);
        }
        PerformanceDataSnapshot perfData = 
                                    (PerformanceDataSnapshot)session.getAttribute("perfData");

        if (apiType != null) {
            if (apiType.endsWith("SQL")) {
                perfData = (PerformanceDataSnapshot) session.getAttribute("perfData");
                SqlStatistics[] sqlStats = perfData.getSqlStatisticsForLayer(apiType, layerType);
                ArrayList sqlStatisticsByExec = new ArrayList();
                int n = WebConfig.getNumOfSqlQueries();
                SqlStatistics[] topNQueriesByExecution = 
                                        ViewUtil.getTopNQueriesByExecutionTime(sqlStats, n);

                for (int i = 0; i < topNQueriesByExecution.length; i++) {
                    SqlStatisticsFormBean formBean = new SqlStatisticsFormBean();
                    BeanUtils.copyProperties(formBean, topNQueriesByExecution[i]);
                    sqlStatisticsByExec.add(formBean);
                }

                ArrayList sqlStatisticsByCount = new ArrayList();
                SqlStatistics[] topNQueriesByCount = ViewUtil.getTopNQueriesByCount(sqlStats,n);
                for (int i = 0; i < topNQueriesByCount.length; i++) {
                    SqlStatisticsFormBean formBean = new SqlStatisticsFormBean();
                    BeanUtils.copyProperties(formBean, topNQueriesByCount[i]);
                    sqlStatisticsByCount.add(formBean);
                }
                	
                request.setAttribute("apiType", apiType);
                request.setAttribute("sqlStatisticsByExec", sqlStatisticsByExec);
                request.setAttribute("sqlStatisticsByCount", sqlStatisticsByCount);
                
                return (mapping.findForward("sql"));
            }
            else{
                AggregateExecutionTime[] summary = 
                                 ViewUtil.getSummaryForALayer(request, session, apiType, layerType);
                ArrayList aggrApiList = new ArrayList();

                for (int i = 0; i < summary.length; i++) {
                    AggrExecTimeFormBean formBean = new AggrExecTimeFormBean();
                    BeanUtils.copyProperties(formBean, summary[i]);
                    aggrApiList.add(formBean);
                }

                String exclusiveInclusiveMode = request.getParameter("exclusiveInclusiveMode");
                if (exclusiveInclusiveMode != null){
                    session.setAttribute("exclusiveInclusiveMode", exclusiveInclusiveMode);
                }
                    
                request.setAttribute("summary", summary);
                request.setAttribute("aggrApiList", aggrApiList);
                request.setAttribute("apiType", apiType);

                return (mapping.findForward("continue"));        	
            }            
        }
        
        return null;

    }

}
