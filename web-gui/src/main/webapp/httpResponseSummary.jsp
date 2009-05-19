<!--
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
 * Original Author:  kaushal.kumar (Tavant Technologies)
 * Contributor(s):   prashant.nair;
 *
-->

<%@ page import="net.sf.infrared.web.ui.ViewUtil,
                 net.sf.infrared.base.model.AggregateApiTime"
        contentType="text/html; charset=iso-8859-1" language="java" errorPage="" %>

<%@ taglib uri="struts/logic" prefix="logic" %>       
<%@ taglib uri="struts/bean" prefix="bean" %> 

<jsp:useBean id="isInActiveMode" scope="session" type="java.lang.String"/>
<%
    AggregateApiTime httpSummary = (AggregateApiTime)request.getAttribute("httpSummary");
	String sortBy = request.getParameter("sortBy");
	String sortDir = request.getParameter("sortDir");

%>


<html><!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>InfraRED</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<link href="common/stylesheets/master.css" rel="stylesheet" type="text/css">
<SCRIPT SRC="javascript/functions.js"></SCRIPT>
</head>

<body>
<table width="780" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="50%" class="tavantLogo">&nbsp;</td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td class="noBorderCell"><!-- InstanceBeginEditable name="Navigation1" -->
        <%
            request.setAttribute("highlight","httpSumm");
        %>
        <jsp:include page="includes/tabDetails.jsp"/>

        <!-- InstanceEndEditable --></td>
  </tr>
  <tr>
    <td class="noBorderCell"> <!-- InstanceBeginEditable name="secondNav" -->
      <!-- InstanceEndEditable --></td>
  </tr>
  <tr>
    <td class="noBorderCell"><!-- InstanceBeginEditable name="Error" -->
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="error" style="display:none">Error</td>
        </tr>
      </table>
      <!-- InstanceEndEditable --></td>
  </tr>
  <jsp:include page="includes/applicationNameInfo.jsp" />
  <tr>
    <logic:notEqual name="isInActiveMode" value="false">
        <td class="pageTitle">The application is in active mode</td>
    </logic:notEqual>

    <logic:equal name="isInActiveMode" value="false">
        <%
            String fileName = (String)session.getAttribute("fileName");
        %>
        <td class="pageTitle">The snapshot loaded is <%=fileName%></td>
    </logic:equal>
  <tr>
    <td class="moduleTitle"><!-- InstanceBeginEditable name="pageTitle" -->HTTP Response Summary<!-- InstanceEndEditable --></td>
  </tr>
    <td class="noBorderCell"><!-- InstanceBeginEditable name="content" -->
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold"> URI 
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("uri",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("uri",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                      
                      </td>
                    </tr>
                  </table></td>
                <td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold">Total Time (ms)
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("totalTime",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("totalTime",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                                            
                      </td>
                    </tr>
                  </table></td>
				<td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold"> Count 
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("count",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("count",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                                            
                      </td>
                    </tr>
                  </table></td>
				<td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold"> Avg 
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("time",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("time",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                                            
                      </td>
                    </tr>
                  </table></td>
				<td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold"> Adj Avg <sup>*</sup>
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("adjAvg",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("adjAvg",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                                            
                      </td>
                    </tr>
                  </table></td>                                    
				<td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold"> Min 
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("minTime",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("minTime",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                                            
                      </td>
                    </tr>
                  </table></td>                                    
				<td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold"> Max 
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("maxTime",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("maxTime",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                                            
                      </td>
                    </tr>
                  </table></td>                                    
				<td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold"> First 
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("firstExec",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("firstExec",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                                            
                      </td>
                    </tr>
                  </table></td>                                    
				<td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold"> Last 
						<a href="<%=response.encodeURL(ViewUtil.getSortByHrefInResponse("lastExec",sortDir,sortBy))%>">
                        	<img src="<%=ViewUtil.getSortByIconInResponse("lastExec",sortDir,sortBy)%>" width="15" height="15" border="0">
                      	</a>                                            
                      </td>
                    </tr>
                  </table></td>                                                      
              </tr>
              <logic:iterate id="uriInfo" collection="<%=request.getAttribute(\"uriSummary\")%>">
              <tr>
				<td class="wrappedTD"><bean:write name="uriInfo" property="uriName" /></td>
				<td class="numericTD"><bean:write name="uriInfo" property="totalTime" /></td>
				<td class="numericTD"><bean:write name="uriInfo" property="count" /></td>
				<td class="numericTD"><bean:write name="uriInfo" property="averageTime" /></td>
				<td class="numericTD"><bean:write name="uriInfo" property="adjAverageTime" /></td>
				<td class="numericTD"><bean:write name="uriInfo" property="minTime" /></td>
				<td class="numericTD"><bean:write name="uriInfo" property="maxTime" /></td>
				<td class="numericTD"><bean:write name="uriInfo" property="firstExecTime" /></td>
				<td class="numericTD"><bean:write name="uriInfo" property="lastExecTime" /></td>
              </tr>
              </logic:iterate>
			  <tr>
              <td colspan="9">
                * Adj. Avg - Adjusted average (average excluding the first execution)
               </td>
              </tr>
              
            </table></td>
        </tr>
      </table>
      <!-- InstanceEndEditable --></td>
  </tr>
</table>
</body>
<!-- InstanceEnd --></html>