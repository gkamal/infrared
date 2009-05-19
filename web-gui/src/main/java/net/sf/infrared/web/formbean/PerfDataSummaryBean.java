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
package net.sf.infrared.web.formbean;

import java.util.ArrayList;
import java.util.Set;

import org.apache.struts.action.ActionForm;

public class PerfDataSummaryBean extends ActionForm {
    public Set applicationName;

    public Set instanceName;

    public Set selectedApplications;

    public Set selectedInstances;

    public ArrayList layerTimes;
    
    public ArrayList absoluteLayerTimes;

    public Set getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(Set applicationName) {
        this.applicationName = applicationName;
    }

    public Set getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(Set instanceName) {
        this.instanceName = instanceName;
    }

    public Set getSelectedApplications() {
        return selectedApplications;
    }

    public void setSelectedApplications(Set selectedApplications) {
        this.selectedApplications = selectedApplications;
    }

    public Set getSelectedInstances() {
        return selectedInstances;
    }

    public void setSelectedInstances(Set selectedInstances) {
        this.selectedInstances = selectedInstances;
    }

    public ArrayList getLayerTimes() {
        return layerTimes;
    }

    public void setLayerTimes(ArrayList layerTimes) {
        this.layerTimes = layerTimes;
    }

    public ArrayList getAbsoluteLayerTimes() {
        return absoluteLayerTimes;
    }

    public void setAbsoluteLayerTimes(ArrayList absoluteLayerTimes) {
        this.absoluteLayerTimes = absoluteLayerTimes;
    }
    
    

}
