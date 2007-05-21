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
 * Original Author: kamal.govindraj (Tavant Technologies) 
 * Contributor(s):  binil.thomas
 *
 */
package net.sf.infrared.agent;

import net.sf.infrared.base.model.ExecutionTimer;
import net.sf.infrared.base.util.LoggingFactory;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

import org.apache.log4j.Logger;

/**
 * 
 * @author kamal.govindraj
 * @author binil.thomas
 */
public class TreeBuilder {
    private static final Logger log = LoggingFactory.getLogger(TreeBuilder.class);

    private Tree tree = new Tree();

    // This treeNode represents the current operation in the operation tree.
    private TreeNode currNode = null;

    private long pruneThreshold = -1;

    public void begin(ExecutionTimer timer) {
        TreeNode node = TreeNode.createTreeNode(timer);
        if (currNode == null) {
            tree.setRoot(node);
            if (log.isDebugEnabled()) {
                log.debug(this + " - Added node " + timer + " as root");
            }
        } else {
            currNode.addChild(node);
        }
        currNode = node;
    }

    public boolean end(ExecutionTimer timer) {
        if (isCallTraceFaulty(timer)) {
            log.error(this + " - Call trace is faulty");
            return false;
        }

        TreeNode childNode = currNode;
        currNode = currNode.getParent();

        // Method time was below the pruneBelowTime, hence we need to remove it
        if (timer.getInclusiveTime() <= getPruneBelowTime()) {            
            removeFromCallTrace(childNode);
            if (log.isDebugEnabled()) {
                log.debug("Removed execution " + timer.getContext() + " from tree " +
                        " because the time (" + timer.getInclusiveTime() + 
                        ") <= prune threshold (" + getPruneBelowTime() + ")");
            }
        }

        return true;
    }

    public Tree reset() {
        if (log.isDebugEnabled()) {
            log.debug(this + " - Resetting tree");
        }
        Tree oldTree = tree;
        tree = new Tree();
        currNode = null;
        return oldTree;
    }

    public String toString() {
        return "TreeBuilder for thread " + Thread.currentThread();
    }

    long getPruneBelowTime() {
        return pruneThreshold;
    }

    void setPruneBelowTime(long time) {
        pruneThreshold = time;
    }

    private void removeFromCallTrace(TreeNode childNode) {
        if (currNode != null) {
            currNode.removeChild(childNode);
        }
    }

    private boolean isCallTraceFaulty(ExecutionTimer currentExec) {
        if (currNode == null) {
            return true;
        }

        ExecutionTimer expectedExec = (ExecutionTimer) currNode.getValue();

        // if (! currentExec.getEvent().equals( expectedExec.getEvent() )) {
        if (currentExec != expectedExec) {
            if (log.isDebugEnabled()) {
                log.debug(this + " - Mismatch on stack for Thread "
                        + Thread.currentThread().getName() + ". Expected "
                        + expectedExec.getContext() + ", encountered " + currentExec.getContext());
            }
            return true;
        }
        return false;
    }
}
