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

import java.util.HashSet;

import net.sf.jsptree.tree.TreeStateModel;

/**
 * The class which stores the current state of the tree. <br/>
 * It keeps track that which nodes in the tree are exploded/collapsed.
 */
public class IRTreeStateModel implements TreeStateModel
{
    private HashSet selected;
    private HashSet opened;
    private boolean expand;

    public IRTreeStateModel()
    {
        selected = new HashSet();
        opened = new HashSet();
    }

    public void addOpened(int hashCode)
    {
        opened.add(new Integer(hashCode));
    }

    public void addSelected(int hashCode)
    {
        selected.add(new Integer(hashCode));
    }

    public boolean isSelected(int code)
    {
        return selected.contains(new Integer(code));
    }

    public boolean isOpened(int code)
    {
        return expand || opened.contains(new Integer(code));
    }

    public void removeSelected(int code)
    {
        selected.remove(new Integer(code));
    }

    public void removeOpened(int code)
    {
        opened.remove(new Integer(code));
    }

    public void removeAllSelected()
    {
        selected.clear();
    }

    public void removeAllOpened()
    {
        opened.clear();
    }

    public void expand(String expandAll)
    {
        if("full".equals(expandAll))
            expand = true;
        else
            expand = false;
    }

    public Object clone() throws CloneNotSupportedException
    {
        IRTreeStateModel stateModel = (IRTreeStateModel) super.clone();
        stateModel.selected = (HashSet) this.selected.clone();
        stateModel.opened = (HashSet) this.opened.clone();
        return stateModel;
    }

}
