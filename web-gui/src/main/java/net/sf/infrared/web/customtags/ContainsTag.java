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
package net.sf.infrared.web.customtags;

import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.RequestUtils;

public class ContainsTag extends TagSupport {
    protected String name;

    protected String property;

    protected String scope;

    protected String selectedName;

    protected String notContains;

    protected boolean flag = false;

    private static final Logger logger = LoggingFactory.getLogger(ContainsTag.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String instanceName) {
        this.selectedName = instanceName;
    }

    public String isNotContains() {
        return notContains;
    }

    public void setNotContains(String notContains) {
        this.notContains = notContains;
    }

    public int doStartTag() throws JspException {
        Object formBean = RequestUtils.lookup(pageContext, name, scope);

        // Able to get an value for instance as Struts creates page context variable
        // with the same name as the id in the logic:iterate tag. Thus for each iteration
        // the value of this variable is updated.

        String instance = (String) RequestUtils.lookup(pageContext, selectedName, scope);
        flag = Boolean.valueOf(notContains).booleanValue();

        Set currentInstance = null;
        try {
            currentInstance = (Set) PropertyUtils.getProperty(formBean, property);
        }
        catch (Exception e) {
            logger.error("Unable to find the property " + property + " in the bean", e);
        }

        if ((currentInstance.contains(instance) && !flag)
                || (!currentInstance.contains(instance) && flag))
            return (EVAL_BODY_INCLUDE);
        else
            return (SKIP_BODY);
    }

    public int doEndTag() throws JspException {
        release();
        return super.doEndTag();
    }

    public void release() {
        super.release();
        name = null;
        property = null;
        scope = null;
        selectedName = null;
        notContains = null;
    }

}
