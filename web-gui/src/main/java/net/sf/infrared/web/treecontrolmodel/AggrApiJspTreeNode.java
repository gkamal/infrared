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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   prashant.nair;
 *
 */
package net.sf.infrared.web.treecontrolmodel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.jsptree.component.JSPTreeNode;

/**
 *  Represents the data in the node. This node is used to display the AggregateApiTime
 */
public class AggrApiJspTreeNode implements JSPTreeNode {
	private AggregateExecutionTime apiTime;

	private NodeToStringConverter nodeToStringConverter;

	private String uniqueKey;

	private Map map;

	static int keyCounter = 0;

	public AggrApiJspTreeNode(AggregateExecutionTime apiTime,
													NodeToStringConverter nodeToStringConverter) {
		this.apiTime = apiTime;
		uniqueKey = Integer.toString(keyCounter++);
		map = new HashMap();
		this.nodeToStringConverter = nodeToStringConverter;
	}

	public AggregateExecutionTime getAggregateApiTime() {
		return apiTime;
	}

	public String getId() {
		return uniqueKey;
	}

	public String getHref() {
		return nodeToStringConverter.getHref(apiTime);
	}

	public boolean isContextRelative() {
		return nodeToStringConverter.isContextRelative();
	}

	public String getTarget() {
		return "";
	}

	public String getLabel() {
		return nodeToStringConverter.toString(apiTime);
	}

	public String getSkinName() {
		return null;
	}

	public Map getValues() {
		return Collections.unmodifiableMap(map);
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean equals(Object p_obj) {
		if (!(p_obj instanceof AggrApiJspTreeNode)) {
			return false;
		}
		AggrApiJspTreeNode a_treeNode = (AggrApiJspTreeNode) p_obj;
		return uniqueKey.equals(a_treeNode.getId());
	}

	public int hashCode() {
		return 37 * uniqueKey.hashCode();
	}
}
