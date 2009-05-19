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
 * Original Author: kaushal.kumar (Tavant Technologies)
 * Contributor(s):  prashant.nair;
 *
 */
package net.sf.infrared.web.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.infrared.base.model.LayerTime;
import net.sf.infrared.web.formbean.ConfigureFormBean;
import net.sf.infrared.web.formbean.LayerTimeBean;
import net.sf.infrared.web.formbean.PerfDataSummaryBean;
import net.sf.infrared.web.util.DataFetchUtil;
import net.sf.infrared.web.util.PerformanceDataSnapshot;
import net.sf.infrared.web.util.ViewUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PerfDataSummaryAction extends Action {
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	ConfigureFormBean configBean = (ConfigureFormBean) form;
    	
        HttpSession session = request.getSession();
        Set selectedApplications = new HashSet();
        boolean ascending = true;
        boolean ascendingAbs = true;

        PerfDataSummaryBean perfBean = new PerfDataSummaryBean();
        Map hierarchicalLayerTimes;
        Map absoluteLayerTimes;
        LayerTime [] hierarchicalLayerTimeArr = null;
        LayerTime [] absoluteLayerTimeArr = null;

        if (configBean.isLiveData()) {
            session.setAttribute("isInActiveMode", "true");
            Set applicationNames = DataFetchUtil.getApplicationNames();
            PerformanceDataSnapshot currentPerfData = 
                            (PerformanceDataSnapshot) session.getAttribute("perfData");
            if (request.getParameterValues("applicationName") != null) {
                String[] parameterValues = request.getParameterValues("applicationName");
                for (int i = 0; i < parameterValues.length; i++){
                    selectedApplications.add(parameterValues[i]);
                }                    
            } 
            else if (currentPerfData != null) {
                selectedApplications = currentPerfData.getApplicationNames();
            } 
            else if (applicationNames.size() > 0) {
                selectedApplications.add(applicationNames.iterator().next());
            }

            Set instances = DataFetchUtil.getInstanceNames(selectedApplications);
            Set selectedInstances = new HashSet();
            if (request.getParameter("instanceName") != null) {
                String[] parameterValues = request.getParameterValues("instanceName");
                for (int i = 0; i < parameterValues.length; i++){
                    selectedInstances.add(parameterValues[i]);
                }                    
            } 
            else if (currentPerfData != null) {
                selectedInstances = currentPerfData.getInstanceNames();
            } 
            else if (instances.size() > 0) {
                selectedInstances.add(instances.iterator().next());
            }

            if ("true".equals(request.getParameter("reset"))) {
                selectedApplications.clear();
                selectedInstances.clear();
                applicationNames.clear();
                instances.clear();
                session.setAttribute("perfData", DataFetchUtil.reset());
            }

            // -- creating a application-name to instance-name map --//
            String map[][] = new String[applicationNames.size()][];
            getAppNameToInstNameMapping(applicationNames, map);
            request.setAttribute("map", map);
            
            PerformanceDataSnapshot perfData = 
                                DataFetchUtil.getPerfData(selectedApplications, selectedInstances);

            perfBean.setApplicationName(applicationNames);
            perfBean.setInstanceName(instances);
            perfBean.setSelectedApplications(selectedApplications);
            perfBean.setSelectedInstances(selectedInstances);

            session.setAttribute("perfData", perfData);
            
            hierarchicalLayerTimes = getHierarchicalLayerTimesMap(perfData);  
            absoluteLayerTimes = getAbsoluteLayerTimesMap(perfData);
            String sortBy = getSortBy(request.getParameter("sortBy"));
            ascending = getSortDir(request.getParameter("sortDir"));
            
            String sortByAbs = getSortBy(request.getParameter("sortByAbs"));
            ascendingAbs = getSortDir(request.getParameter("sortDirAbs"));

            hierarchicalLayerTimeArr = new LayerTime[hierarchicalLayerTimes.size()];
            absoluteLayerTimeArr = new LayerTime[absoluteLayerTimes.size()];
            
            int count = 0;
            for(Iterator itr = hierarchicalLayerTimes.values().iterator(); itr.hasNext();){
            	hierarchicalLayerTimeArr[count] = (LayerTime)itr.next();
            	count++;
            }
            count = 0;
            for(Iterator itr = absoluteLayerTimes.values().iterator(); itr.hasNext();){
                absoluteLayerTimeArr[count] = (LayerTime)itr.next();
                count++;
            }

            ViewUtil.sort(hierarchicalLayerTimeArr,sortBy, ascending);
            ViewUtil.sort(absoluteLayerTimeArr,sortByAbs, ascendingAbs);
        } 
        else {
        	String start = configBean.getStartDate();
        	String end = configBean.getEndDate()+" 23:59:59";
            session.setAttribute("isInActiveMode", "false");
            PerformanceDataSnapshot perfData = 
                (PerformanceDataSnapshot) session.getAttribute("perfData");
            if(perfData == null){
            	perfData = new PerformanceDataSnapshot();
            }
        	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat endFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date startDate = formatter.parse(start);
            Date endDate = endFormatter.parse(end);
            Set applicationNames = perfData.getApplicationNames();
            Set instanceNames = perfData.getInstanceNames();
            
            perfData = DataFetchUtil.getDataFromDB(applicationNames, instanceNames,
            								startDate, endDate);
            
            session.setAttribute("perfData", perfData);
            perfBean.setApplicationName(applicationNames);
            perfBean.setInstanceName(instanceNames);
            perfBean.setSelectedApplications(applicationNames);
            perfBean.setSelectedInstances(instanceNames);

            hierarchicalLayerTimes = getHierarchicalLayerTimesMap(perfData);
            absoluteLayerTimes = getAbsoluteLayerTimesMap(perfData);
            
            String sortBy = getSortBy(request.getParameter("sortBy"));
            ascending = getSortDir(request.getParameter("sortDir"));
            
            String sortByAbs = getSortBy(request.getParameter("sortByAbs"));
            ascendingAbs = getSortDir(request.getParameter("sortDirAbs"));
            hierarchicalLayerTimeArr = new LayerTime[hierarchicalLayerTimes.size()];
            absoluteLayerTimeArr = new LayerTime[absoluteLayerTimes.size()];
            int count = 0;
            for(Iterator itr = hierarchicalLayerTimes.values().iterator(); itr.hasNext();){
            	hierarchicalLayerTimeArr[count] = (LayerTime)itr.next();
            	count++;
            }
            count = 0;
            for(Iterator itr = absoluteLayerTimes.values().iterator(); itr.hasNext();){
                absoluteLayerTimeArr[count] = (LayerTime)itr.next();
                count++;
            }

            ViewUtil.sort(hierarchicalLayerTimeArr, sortBy, ascending);
            ViewUtil.sort(absoluteLayerTimeArr, sortByAbs, ascendingAbs);
        }

        ArrayList hierarchicalLayerTimeBean = new ArrayList();    

		for (int i = 0; i < hierarchicalLayerTimeArr.length; i++) {
            LayerTimeBean layerBean = new LayerTimeBean();
            BeanUtils.copyProperties(layerBean, hierarchicalLayerTimeArr[i]);
            hierarchicalLayerTimeBean.add(layerBean);			
		}

        ArrayList absoluteLayerTimeBean = new ArrayList();
        for (int i = 0; i < absoluteLayerTimeArr.length; i++) {
            LayerTimeBean layerBean = new LayerTimeBean();
            BeanUtils.copyProperties(layerBean, absoluteLayerTimeArr[i]);
            absoluteLayerTimeBean.add(layerBean);           
        }

        perfBean.setLayerTimes(hierarchicalLayerTimeBean);
        perfBean.setAbsoluteLayerTimes(absoluteLayerTimeBean);
        request.setAttribute("perfBean", perfBean);
        return (mapping.findForward("continue"));
    }

	private void getAppNameToInstNameMapping(Set applicationNames, String [][] map) {
		Set appName = new HashSet();
		int i = 0;
		for (Iterator itr1 = applicationNames.iterator(); itr1.hasNext();) {
		    int j = 0;
		    String app = (String) itr1.next();       
		    appName.add(app);
		    Set tempInstance = DataFetchUtil.getInstanceNames(appName);
		    appName.remove(app);
		    map[i] = new String[tempInstance.size()];
		    for (Iterator itr2 = tempInstance.iterator(); itr2.hasNext();) {
		        String instanceName = (String)itr2.next();
		        map[i][j++] = instanceName;
		    }
		    i++;
		}
	}

    private Map getHierarchicalLayerTimesMap(PerformanceDataSnapshot perfData) {
        Map layerTimes = new HashMap();
        String[] layers = perfData.getStats().getHierarchicalLayers();
        for (int j = 0; j < layers.length; j++) {
            String aLayer = layers[j];
            long itsTime = perfData.getStats().getTimeInHierarchicalLayer(aLayer);
            LayerTime lt = new LayerTime(aLayer);
            lt.setTime(itsTime);
            layerTimes.put(aLayer, lt);
        }
        return layerTimes;
    }

    private Map getAbsoluteLayerTimesMap(PerformanceDataSnapshot perfData) {
        Map layerTimes = new HashMap();
        String[] layers = perfData.getStats().getAbsoluteLayers();
        for (int j = 0; j < layers.length; j++) {
            String aLayer = layers[j];
            long itsTime = perfData.getStats().getTimeInAbsoluteLayer(aLayer);
            LayerTime lt = new LayerTime(aLayer);
            lt.setTime(itsTime);
            layerTimes.put(aLayer, lt);
        }
        return layerTimes;
    }

    private String getSortBy(String sortBy){
        if (sortBy == null) {
            sortBy = "time";
        }
        return sortBy;
    }
    
    private boolean getSortDir(String sortDir){
        boolean dir = true;
        if (sortDir != null && sortDir.equals("desc")) {
            dir = false;
        }
        if (sortDir == null) {
            sortDir = "desc";
            dir = false;
        }
        return dir;
    }

}
