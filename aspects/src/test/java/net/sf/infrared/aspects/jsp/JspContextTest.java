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
 * Original Author:  binil.thomas (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.aspects.jsp;

import junit.framework.TestCase;
import net.sf.infrared.base.model.ExecutionContext;

public class JspContextTest extends TestCase {
    public void testWeblogicJspName() {        
        String actual = JspContext.getWeblogicJspName("jsp_servlet._test.__one_45_1");                
        String expected = "test/one-1.jsp";
        assertEquals(expected, actual);             
        
        actual = JspContext.getWeblogicJspName("jsp_servlet.__one_45_1");        
        expected = "one-1.jsp";
        assertEquals(expected, actual);
        
        actual = JspContext.getWeblogicJspName("jsp_servlet._test_45_1._test_1.__one_126_1");        
        expected = "test-1/test_1/one~1.jsp";
        assertEquals(expected, actual);
    }
    
    public void testJasperJspName() {        
        String actual = JspContext.getJasperJspName("org.apache.jsp.one_007e1_jsp");                
        String expected = "one~1.jsp";
        assertEquals(expected, actual);             
                        
        actual = JspContext.getJasperJspName("org.apache.jsp.test_002d1.one_002d1_jsp");                        
        expected = "test-1/one-1.jsp";
        assertEquals(expected, actual);
        
        
        actual = JspContext.getJasperJspName("org.apache.jsp.test_002d1.test_005f1.one_007e1_jsp");                        
        expected = "test-1/test_1/one~1.jsp";        
        assertEquals(expected, actual);
        
        
        actual = JspContext.getJasperJspName("org.apache.jsp.foo_005fbar_jsp");                        
        expected = "foo_bar.jsp";        
        assertEquals(expected, actual);        
                        
        actual = JspContext.getJasperJspName("org.apache.jsp.foo_005fnormal_jsp");                        
        expected = "foo_normal.jsp";        
        assertEquals(expected, actual);
    }   
    
    public void testJspContextEquals() {        
        ExecutionContext newApi1 = new JspContext("com.em.Api1", "Jsp"); 
        ExecutionContext api1 = new JspContext("com.em.Api1");        
        assertEquals(newApi1, api1);        
    }
    
}
