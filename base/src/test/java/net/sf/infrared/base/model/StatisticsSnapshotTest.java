/*
 * PerformanceStatisticsTest.java
 * JUnit based test
 *
 * Created on November 26, 2005, 6:58 PM
 */

package net.sf.infrared.base.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.infrared.base.util.Tree;
import net.sf.infrared.base.util.TreeNode;

/**
 *
 * @author binil.thomas
 */
public class StatisticsSnapshotTest extends TestCase {
    
    public StatisticsSnapshotTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(StatisticsSnapshotTest.class);
        
        return suite;
    }
    
    public void testAddToLastInvocationsList() {
        StatisticsSnapshot fixture = new StatisticsSnapshot();
        fixture.setMaxLastInvocations(4); // only four tree shd be held
        assertEquals("", 0, fixture.getLastInvocations().size()); // initially no data
        
        // add the first list of trees        
        List list1 = new ArrayList();
        Tree tree1 = createTree("tree1"); 
        Tree tree2 = createTree("tree2"); 
        list1.add(tree1);
        list1.add(tree2);
        fixture.addToLastInvocations(list1);
        List out = fixture.getLastInvocations();
        assertEquals("", 2, out.size());        
        assertTrue(out.contains(tree1));
        assertTrue(out.contains(tree2));
        
        // add a second list of trees
        List list2 = new ArrayList();
        Tree tree3 = createTree("tree3"); 
        Tree tree4 = createTree("tree4");        
        list2.add(tree3);
        list2.add(tree4);
        fixture.addToLastInvocations(list2);
        out = fixture.getLastInvocations();
        assertEquals("", 4, out.size());       
        assertTrue(out.contains(tree1));
        assertTrue(out.contains(tree2));
        assertTrue(out.contains(tree3));
        assertTrue(out.contains(tree4));
        
        // add a third one - this time the first couple of trees shd be pushed out        
        List list3 = new ArrayList();        
        Tree tree5 = createTree("tree5"); 
        Tree tree6 = createTree("tree6");
        Tree tree7 = createTree("tree7");
        list3.add(tree5);
        list3.add(tree6);
        list3.add(tree7);
        fixture.addToLastInvocations(list3);
        out = fixture.getLastInvocations();
        assertEquals("", 4, out.size());        
        assertFalse(out.contains(tree1));
        assertFalse(out.contains(tree2));
        assertFalse(out.contains(tree3));
        assertTrue(out.contains(tree4));
        assertTrue(out.contains(tree5));
        assertTrue(out.contains(tree6));
        assertTrue(out.contains(tree7));
    }
    
    private Tree createTree(String nodeName) {
        TreeNode node = TreeNode.createTreeNode(nodeName);
        return new Tree(node);
    }
}
