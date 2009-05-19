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

import net.sf.jsptree.tree.TreeNode;
import net.sf.jsptree.tree.TreeStructureModel;

/**
 * Defines the structure model of the tree.
 */
public class IRTreeStructureModel implements TreeStructureModel
{
	public TreeNode rootNode;
	private String treeName;

	public IRTreeStructureModel(TreeNode node)
	{
		setRootNode(node);
	}

	public void setRootNode(TreeNode node) {
		rootNode = node;
	}

    public String getName()
	{
		return treeName;
	}

    public void setName(String treeName)
	{
		this.treeName = treeName;
	}

    public TreeNode getRootNode()
	{
		return rootNode;
	}

    public void setRoot(Object root){}

    public boolean addChildToRoot(Object child)
    {
        return false;
    }

    public boolean addChildToParent(Object parent, Object child)
    {
        return false;
    }

    public boolean removeNode(Object node)
    {
        return false;
    }

    public boolean isLeaf(Object node)
    {
        return false;
    }

    public int getChildCount(Object parent)
    {
        return 0;
    }

    public int getMaxDepth()
    {
        return 0;
    }

    public Object clone() throws CloneNotSupportedException
    {
        IRTreeStructureModel structure = (IRTreeStructureModel)super.clone();
        if(this.rootNode != null)
            structure.rootNode = (TreeNode)this.rootNode.clone();

        return structure;

    }

}
