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

import java.io.File;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.infrared.aspects.AbstractExecutionContext;

public class JspContext extends AbstractExecutionContext {
    private static Properties cache = new Properties();
    
    private static final String JASPER_PREFIX = "org.apache.jsp.";
    
    private static final String WEBLOGIC_PREFIX = "jsp_servlet._";
    
    private String jspName;
    
    // private String jspClassName;
    
    
    
    public JspContext(String jspClassName) {
        super("Jsp");
        if (jspClassName.startsWith(JASPER_PREFIX)) {
            jspName = JspContext.getJasperJspName(jspClassName);
        } else if (jspClassName.startsWith(WEBLOGIC_PREFIX)) {
            jspName = JspContext.getWeblogicJspName(jspClassName);
        } else {
            jspName = jspClassName;
        }        
    }
    
    public JspContext(Class jspClass) {
        this(jspClass.getName());
    }
    
    public JspContext(String name, String layer) {
        super(layer);
        
        this.jspName = name;
        // this.jspClassName = name;
    }
    
    public String getName() {
        if (jspName == null) {                                            
            // @TODO make it work for other JSP engines
            // @TODO consider making this a strategy
            
        }
        return jspName;
    }
    
    public String toString() {
        return "Jsp " + getName();
    }
    
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (o == this) {
            return true;
        }
        
        if (! (o instanceof JspContext) ) {
            return false;
        }
        
        JspContext other = (JspContext) o;
        
        return other.jspName.equals( this.jspName );
    }
    
    public int hashCode() {
        return 7 * jspName.hashCode();
    }
    
    //@TODO uses Java 1.4 APIs; if someone needs to use InfraRED with 1.3, we can rewrite these
    // portions with Jakarta Regexp package
    
    static String getJasperJspName(String jspClassName) {
        String jspName = (String) cache.get(jspClassName);
        if (jspName != null) {
            return jspName;
        }
        jspName = jspClassName.substring(JASPER_PREFIX.length());
        
        // jasper replaces a directory seperator with a "."
        jspName = jspName.replace('.', '/');
        
        // jasper replaces special characters with a mangle character,
        // which is '_' followed by the hexadecimal value of the character
        Pattern pattern = Pattern.compile("_[a-f0-9]{4}");
        Matcher matcher = pattern.matcher(jspName);
        int findFrom = 0;
        while (matcher.find(findFrom)) {          
            findFrom = matcher.start() + 1;
            String mangledSpecialChar = matcher.group();
            mangledSpecialChar = mangledSpecialChar.substring(1, mangledSpecialChar.length());
            String unmangedSpecialChar = JspContext.getUnmangedSpecialChar(mangledSpecialChar);            
            jspName = jspName.replaceAll("_" + mangledSpecialChar, unmangedSpecialChar);            
            matcher = pattern.matcher(jspName);
        }
        
        // jasper replaces '.' with a '_'
        jspName = jspName.substring(0, jspName.length() - 4);
        jspName = jspName + ".jsp";
        cache.put(jspClassName, jspName);
        return jspName;
    }
    
    static String getWeblogicJspName(String jspClassName) {
        String jspName = (String) cache.get(jspClassName);
        if (jspName != null) {
            return jspName;
        }
        
        jspName = jspClassName.substring(WEBLOGIC_PREFIX.length());
        
        // weblogic replaces a directory seperator with a "._"        
        jspName = jspName.replaceAll("\\._", "/");
        
        // weblogic replaces every special character with _[xxx]_ where xxx is the integer
        // value of the special character
        Pattern pattern = Pattern.compile("_[0-9]+_");
        Matcher matcher = pattern.matcher(jspName);
        while (matcher.find()) {
            String specialCharMangle = matcher.group();
            specialCharMangle = specialCharMangle.substring(1, specialCharMangle.length() - 1);
            int ch = Integer.parseInt(specialCharMangle);            
            jspName = jspName.replaceAll("_" + specialCharMangle + "_", String.valueOf((char) ch));
            matcher = pattern.matcher(jspName);
        }
        
        // weblogic adds leading _ to file & directory names
        jspName = jspName.replaceAll("/_", "/");
        jspName = jspName.replaceAll("^_", "");
        jspName = jspName + ".jsp";
        
        cache.put(jspClassName, jspName);
        return jspName;
    }
    
    // @TODO most likely there is some better way to figure this out using shift operators
    // fix this later!
    static String getUnmangedSpecialChar(String mangled) {                        
        char ch = mangled.charAt(0);
        int hex0 = Character.digit(ch, 16);
        ch = mangled.charAt(1);
        int hex1 = Character.digit(ch, 16);
        ch = mangled.charAt(2);
        int hex2 = Character.digit(ch, 16);
        ch = mangled.charAt(3);
        int hex3 = Character.digit(ch, 16);
        int sum = (4096 * hex0) + (256 * hex1) + (16 * hex2) + hex3; 
        
        return new String(new char[] {(char) sum});
    }
}
