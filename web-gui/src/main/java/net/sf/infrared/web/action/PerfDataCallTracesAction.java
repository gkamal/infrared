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

import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.AggregateOperationTree;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.util.TreeNode;
import net.sf.infrared.web.formbean.AggrExecTimeFormBean;
import net.sf.infrared.web.formbean.SqlStatisticsFormBean;
import net.sf.infrared.web.treecontrolmodel.NodeToStringConverter;
import net.sf.infrared.web.treecontrolmodel.TreeFactoryImpl;
import net.sf.infrared.web.util.PerformanceDataSnapshot;
import net.sf.infrared.web.util.SqlStatistics;
import net.sf.infrared.web.util.TreeUtil;
import net.sf.infrared.web.util.ViewUtil;
import net.sf.jsptree.tree.TreeFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PerfDataCallTracesAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
						HttpServletResponse response)
						throws IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();

		TreeFactory treeFactory = null;
		String apiName = request.getParameter("api");
		if (apiName != null) {
			session.setAttribute("api", apiName);
		}

        // This parameter corresponds to the actual layer name to
        // which the api belongs
        String actualLayerName = request.getParameter("layerName");
        if(actualLayerName != null){
            session.setAttribute("layerName", actualLayerName);
        }


		String className = request.getParameter("ctx");

        if(className != null){
            session.setAttribute("ctx", className);
        }

        // This parameter corresponds to hierarchical layer if the navigation
        // is in the context of the hierarchical layer. Else it corresponds to
        // the absolute layer.

		String type = request.getParameter("type");
		if (type != null) {
			session.setAttribute("type", type);
		}

		final PerformanceDataSnapshot perData =
									(PerformanceDataSnapshot) session.getAttribute("perfData");
        final String layerType = (String)session.getAttribute("layerType");

		AggregateOperationTree aggrOptree = perData.getStats().getTree();
        String hierarchicalLayerName = null;
        if(layerType.equals("hier")){
            hierarchicalLayerName = type;
        }
		TreeNode mergedHead = TreeUtil.getMergedExecutionContextTreeNode(aggrOptree, apiName,
												actualLayerName, className, hierarchicalLayerName);

		if(mergedHead != null) {

			final long OVERALL_TIME =
						((AggregateExecutionTime)mergedHead.getValue()).getTotalInclusiveTime();
			final NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);


			NodeToStringConverter nodeToStringConverterMergedTree = new NodeToStringConverter() {
				public String toString(AggregateExecutionTime aggApiTime) {
					StringBuffer display = new StringBuffer();
					if(OVERALL_TIME == 0){
						display.append(aggApiTime.getContext().toString())
							   .append(" [ ")
							   .append(" Total Time = ")
							   .append(aggApiTime.getTotalInclusiveTime())
							   .append(" Exclusive Time = ")
							   .append(aggApiTime.getTotalExclusiveTime())
							   .append(" Count = ")
							   .append(aggApiTime.getExecutionCount())
							   .append(" ] ");
					}
					else{
						display.append(aggApiTime.getContext().toString())
							   .append(" [ ")
							   .append(nf.format((aggApiTime.getTotalInclusiveTime() * 100.0)
																/ OVERALL_TIME))
							   .append("% -")
							   .append(" Total Time = ")
							   .append(aggApiTime.getTotalInclusiveTime())
							   .append(" Exclusive Time = ")
							   .append(aggApiTime.getTotalExclusiveTime())
							   .append(" Count = ")
							   .append(aggApiTime.getExecutionCount())
							   .append(" ] ");

					}
					return display.toString();
				}

				public String getHref(AggregateExecutionTime apiTime) {
					ExecutionContext ctx = apiTime.getContext();
					String name = ctx.getName();
					String layer = ctx.getLayer();
					String layerName ="";
					String temp = null;
					if(layerType.equals("abs")){
						layerName = ctx.getLayer();
					}
					else if(layerType.equals("hier")){
						layerName = apiTime.getLayerName();
					}

					try{
						temp = "perfData_apiSumm_callTracesAction.do?" + "api="+ URLEncoder.encode(name, "UTF-8") +
								"&type="+ layerName +
								"&ctx="+ ctx.getClass().getName()+
								"&layerName="+ layer;
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					return temp;
				}

				public boolean isContextRelative() {
					return false;
				}
			};

			String tree = "InfraTree";
			treeFactory = new TreeFactoryImpl(mergedHead, tree, nodeToStringConverterMergedTree);
		}

        AggregateExecutionTime[] mergedTreeJdbcSummaries = new AggregateExecutionTime[0];
		SqlStatistics[] mergedTreeSqlStatistics = new SqlStatistics[0];


		if (mergedHead != null) {
			mergedTreeJdbcSummaries = ViewUtil.getJDBCSummary(mergedHead);
			mergedTreeSqlStatistics = ViewUtil.getSqlStatistics(mergedHead);
		}

		ArrayList jdbcSummaries = new ArrayList();

		for (int i = 0; i < mergedTreeJdbcSummaries.length; i++) {
			AggrExecTimeFormBean formBean = new AggrExecTimeFormBean();
			BeanUtils.copyProperties(formBean, mergedTreeJdbcSummaries[i]);
			jdbcSummaries.add(formBean);
		}

		ArrayList sqlStatisticsByExec = new ArrayList();
		int n = 5;
		SqlStatistics[] topNQueriesByExecution =
						ViewUtil.getTopNQueriesByExecutionTime(mergedTreeSqlStatistics, n);

		for (int i = 0; i < topNQueriesByExecution.length; i++) {
			SqlStatisticsFormBean formBean = new SqlStatisticsFormBean();
			BeanUtils.copyProperties(formBean, topNQueriesByExecution[i]);
			sqlStatisticsByExec.add(formBean);
		}

		ArrayList sqlStatisticsByCount = new ArrayList();
		SqlStatistics[] topNQueriesByCount =
									ViewUtil.getTopNQueriesByCount(mergedTreeSqlStatistics, n);

		for (int i = 0; i < topNQueriesByCount.length; i++) {
			SqlStatisticsFormBean formBean = new SqlStatisticsFormBean();
			BeanUtils.copyProperties(formBean, topNQueriesByCount[i]);
			sqlStatisticsByCount.add(formBean);
		}

		// These have been put in the session because the expansion
		// and contraction of the tree cause a new request which does
		// go through the struts action. The page is directly called.

		session.setAttribute("mergedHead", mergedHead);
		session.setAttribute("jdbcSummaries", jdbcSummaries);
		session.setAttribute("sqlStatisticsByExec", sqlStatisticsByExec);
		session.setAttribute("sqlStatisticsByCount", sqlStatisticsByCount);

		session.setAttribute("treeFactory", treeFactory);
		return mapping.findForward("repeat");
	}

}
