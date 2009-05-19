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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.web.formbean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.util.TreeNode;
import net.sf.infrared.web.util.ViewUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;

public class LastInvocationTreeBean extends ActionForm {
	
	private TreeNode node;
	private ArrayList aggregateApiBean;
    private String jdbcSummaryLength;
    private String sqlStatsLength;
	
	
	public LastInvocationTreeBean(TreeNode node) throws IllegalAccessException, 
																	InvocationTargetException{
		this.setNode(node);
		AggregateExecutionTime[] jdbcSummaries = ViewUtil.getJDBCSummary(node);
		
        aggregateApiBean = new ArrayList();

        for (int i = 0; i < jdbcSummaries.length; i++) {
			AggrExecTimeFormBean formBean = new AggrExecTimeFormBean();
			BeanUtils.copyProperties(formBean, jdbcSummaries[i]);
			aggregateApiBean.add(formBean);
		}
        
        jdbcSummaryLength = new Integer(jdbcSummaries.length).toString();
	}
	
    public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}

	public ArrayList getAggregateApiBean() {
		return aggregateApiBean;
	}

	public void setAggregateApiBean(ArrayList aggregateApiBean) {
		this.aggregateApiBean = aggregateApiBean;
	}

	public String getJdbcSummaryLength() {
		return jdbcSummaryLength;
	}

	public void setJdbcSummaryLength(String jdbcSummaryLength) {
		this.jdbcSummaryLength = jdbcSummaryLength;
	}

	public String getSqlStatsLength() {
		return sqlStatsLength;
	}

	public void setSqlStatsLength(String sqlStatsLength) {
		this.sqlStatsLength = sqlStatsLength;
	}
		
}
