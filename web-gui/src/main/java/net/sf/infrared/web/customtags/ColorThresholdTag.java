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

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.infrared.web.util.WebConfig;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.util.RequestUtils;

public class ColorThresholdTag extends TagSupport {
    private static final int COLOR_THRESHOLD = WebConfig.getColorThreshold();
    protected String name;

    protected String property;

    protected String scope;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

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

    public int doStartTag() throws JspException {
        Object formBean = RequestUtils.lookup(pageContext, name, scope);

        String beanProperty = null;
        try {
            beanProperty = (String) PropertyUtils.getProperty(formBean, property);
        }
        catch (IllegalAccessException e) {
            throw new JspException("Unable to find the property " + property + " in the bean", e);
        }
        catch (InvocationTargetException e) {
            throw new JspException("Unable to find the property " + property + " in the bean", e);
        }
        catch (NoSuchMethodException e) {
            throw new JspException("Unable to find the property " + property + " in the bean", e);
        }
        double value = Double.parseDouble(beanProperty);

        if (value > COLOR_THRESHOLD)
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
    }

}
