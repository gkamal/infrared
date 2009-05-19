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
 * Original Author: prashant.nair (Tavant Technologies)
 *
 */
package net.sf.infrared.web.formbean;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ConfigureFormBean extends ActionForm {
	
	private String dataFetch = "true";    
	private String startDate;
	private String endDate;
    
    public ConfigureFormBean() {
        super();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        startDate = sdf.format(new Date());
        endDate = startDate;
    }

    public boolean isLiveData() {
		return "true".equalsIgnoreCase(dataFetch);
	}
	
	public String getDataFetch() {
		return dataFetch;
	}

	public void setDataFetch(String typeOfData) {
		this.dataFetch = typeOfData;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		return super.validate(arg0, arg1);
	}
}