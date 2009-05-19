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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.web.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

import org.apache.commons.beanutils.MethodUtils;

/**
 * A simple comparator implementation that enables comparing two
 * bean instances based on a specified property.
 */
public class BeanComparator implements Comparator
{
    String methodName = null;
    boolean ascending = true;
    public BeanComparator(String methodName, boolean ascending)
    {
        this.methodName = methodName;
        this.ascending = ascending;
    }

    public int compare(Object o1, Object o2)
    {
        Object fieldOne = null;
        Object fieldTwo = null;
        try
        {
            fieldOne = MethodUtils.invokeExactMethod(o1,methodName,null);
            fieldTwo = MethodUtils.invokeExactMethod(o2,methodName,null);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }

        if (fieldOne != null
            && fieldTwo != null
            && fieldOne.getClass().equals(fieldTwo.getClass())
            && fieldOne instanceof Comparable
            && fieldTwo instanceof Comparable)
        {
            int retValue = ((Comparable)fieldOne).compareTo(fieldTwo);
            return (ascending ? retValue : (retValue*-1));
        }
        else
        {
            throw new IllegalArgumentException("Error comparing object "
                                               + o1.toString()
                                               + " with "
                                               + o2.toString() );
        }
    }

    public void setAscending(boolean ascending)
    {
        this.ascending = ascending;
    }

}
