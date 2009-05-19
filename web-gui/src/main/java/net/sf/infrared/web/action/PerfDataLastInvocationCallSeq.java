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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.AggregateOperationTree;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;
import net.sf.infrared.web.formbean.LastInvocationTreeBean;
import net.sf.infrared.web.formbean.SqlStatisticsFormBean;
import net.sf.infrared.web.treecontrolmodel.NodeToStringConverter;
import net.sf.infrared.web.treecontrolmodel.TreeFactoryImpl;
import net.sf.infrared.web.util.PerformanceDataSnapshot;
import net.sf.infrared.web.util.SqlStatistics;
import net.sf.infrared.web.util.ViewUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PerfDataLastInvocationCallSeq extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, 
								HttpServletResponse response)
								throws IOException, ServletException, IllegalAccessException,
								InvocationTargetException {
		
		HttpSession session = request.getSession();

		final PerformanceDataSnapshot perfData = (PerformanceDataSnapshot) 
														session.getAttribute("perfData");;
														
        List lastInvTreeList = perfData.getStats().getLastInvocations();
		Tree [] lastInvocationTrees = new Tree[lastInvTreeList.size()];
		lastInvTreeList.toArray(lastInvocationTrees);
		
		NodeToStringConverter nodeToStringConverter = new NodeToStringConverter() {
			public String toString(AggregateExecutionTime aggApiTime) {
				StringBuffer display = new StringBuffer();
				display.append(aggApiTime.getContext().toString()) 
					   .append(" [ ")
					   .append(" Total Time = ")
					   .append(aggApiTime.getTotalInclusiveTime())
                       .append(" Exclusive Time = ")
                       .append(aggApiTime.getTotalExclusiveTime())
					   .append(" Count = ")
					   .append(aggApiTime.getExecutionCount())
					   .append(" ] ");
				return display.toString();
			}

			public String getHref(AggregateExecutionTime apiTime) {
				return "/perfData_lastInvTrees.jsp";
			}

			public boolean isContextRelative() {
				return true;
			}
		};
		
		ArrayList lastInvBeans = new ArrayList();
		ArrayList sqlStatsForAllTrees = new ArrayList();

		for (int i = 0; i < lastInvocationTrees.length; i++) {
			String treeName = "InfraTree" + i;
			ArrayList sqlStatsList = new ArrayList();
			
			// This is done to convert all the ExecutionTimer nodes in the 
			// tree to AggregateExecutionTime
			AggregateOperationTree lastInvocationtree = new AggregateOperationTree();
			lastInvocationtree.merge(lastInvocationTrees[i]);
			
			// The head node is the first child of the root as the root node
			// in an AggregateOperationTree is always a dummy node
			TreeNode headNode = (TreeNode)lastInvocationtree.getAggregateTree().getRoot()
																	 		   .getChildren()
																	 		   .get(0);
			
			TreeFactoryImpl treeFactory = new TreeFactoryImpl(headNode, treeName,
																			nodeToStringConverter);
			session.setAttribute(treeName, treeFactory);
			
			LastInvocationTreeBean treeBean = new LastInvocationTreeBean(headNode);
			
			
			SqlStatistics[] sqlStatistics = ViewUtil.getSqlStatistics(headNode);

			for (int j = 0; j < sqlStatistics.length; j++) {
				SqlStatisticsFormBean formBean = new SqlStatisticsFormBean();
				BeanUtils.copyProperties(formBean, sqlStatistics[j]);
				sqlStatsList.add(formBean);
			}
			treeBean.setSqlStatsLength(new Integer(sqlStatistics.length).toString());
			lastInvBeans.add(treeBean);
			sqlStatsForAllTrees.add(sqlStatsList);
		}

		session.setAttribute("lastInvBeanTree", lastInvBeans);
		session.setAttribute("sqlStats", sqlStatsForAllTrees);

		return mapping.findForward("continue");
	}

}
