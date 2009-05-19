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
                 net.sf.infrared.base.model.jdbc.SqlStatistics,
                 net.sf.infrared.web.InfraREDWebOptions,
                 net.sf.infrared.base.model.AggregateApiTime,
                 java.util.ArrayList"
         contentType="text/html; charset=iso-8859-1" language="java" errorPage=""
%>

<%
    String apiName = (String)request.getAttribute("apiName");
    String apiType = (String)request.getAttribute("apiType");
    AggregateApiTime[] jdbcSummaries = (AggregateApiTime[])request.getAttribute("jdbcSummaries");
    SqlStatistics[] sqlStatistics = (SqlStatistics[])request.getAttribute("sqlStatistics");
%>

<html><!-- InstanceBegin template="/Templates/tavant_jsp.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
<title>InfraRED</title>
<!-- InstanceEndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- InstanceBeginEditable name="head" --><!-- InstanceEndEditable -->
<link href="common/stylesheets/master.css" rel="stylesheet" type="text/css">
<script>
	function showHide(node1,node2)
	{
		if(document.getElementById(node1).style.display=='none')
		{
			document.getElementById(node1).style.display='block';
            document.getElementById(node2).style.display='none';
		}
		else
		{
			document.getElementById(node1).style.display='none';
            document.getElementById(node2).style.display='block';
		}
	}
</script>
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
            request.setAttribute("highlight","summary");
        %>
        <jsp:include page="includes/tabDetails.jsp"/>
    <!-- InstanceEndEditable --></td>
  </tr>
  <tr>
    <td class="noBorderCell"> <!-- InstanceBeginEditable name="secondNav" -->
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="100%" nowrap class="menuUp">&nbsp;</td>
          <td nowrap class="subMenuUp"><a href="<%=response.encodeURL("perfData_use_jspTree.do?api="+apiName+"&type="+apiType)%>" class="menu">Operation Summary</a></td>
          <td nowrap class="subMenuDown">JDBC Summary</a></td>
        </tr>
      </table>
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
  <tr>
    <td class="moduleTitle"><!-- InstanceBeginEditable name="pageTitle" -->Summary of JDBC operations invoked by <%=apiName%><!-- InstanceEndEditable --></td>
  </tr>
    <td class="noBorderCell"><!-- InstanceBeginEditable name="content" -->
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="lablel1Bold"><a href="<%=response.encodeURL("perfData_layerSummaryAction.do?type="+apiType)%>">Go Back To Summary Of Module <%=apiType%></td>
        </tr>
        <tr>
          <td class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="noBorderCell" width="33%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold">API Name</td>
                      <td nowrap class="lablel1">
                      <a href="<%=response.encodeURL("perfData_apiSummary_jdbcAction.do?api="+apiName+"&type="+apiType+"&sortBy=name&sortDir=asc")%>">
                        <img src="common/graphics/sortUp.gif" width="15" height="15" border="0">
                      </a>
                      </td>
                      <td nowrap class="lablel1">
                      <a href="<%=response.encodeURL("perfData_apiSummary_jdbcAction.do?api="+apiName+"&type="+apiType+"&sortBy=name&sortDir=desc")%>">
                        <img src="common/graphics/sortDown.gif" width="15" height="15" border="0">
                      </a>
                      </td>
                    </tr>
                  </table></td>
                <td width="33%" class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold">Average Time (ms)</td>
                      <td nowrap class="lablel1">
                      <a href="<%=response.encodeURL("perfData_apiSummary_jdbcAction.do?api="+apiName+"&type="+apiType+"&sortBy=time&sortDir=asc")%>">
                        <img src="common/graphics/sortUp.gif" width="15" height="15" border="0">
                      </a>
                      </td>
                      <td nowrap class="lablel1">
                      <a href="<%=response.encodeURL("perfData_apiSummary_jdbcAction.do?api="+apiName+"&type="+apiType+"&sortBy=time&sortDir=desc")%>">
                        <img src="common/graphics/sortDown.gif" width="15" height="15" border="0">
                      </a>
                      </td>
                    </tr>
                  </table></td>
                <td width="33%" class="noBorderCell"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="100%" nowrap class="lablel1Bold">Count</td>
                      <td nowrap class="lablel1">
                      <a href="<%=response.encodeURL("perfData_apiSummary_jdbcAction.do?api="+apiName+"&type="+apiType+"&sortBy=count&sortDir=asc")%>">
                        <img src="common/graphics/sortUp.gif" width="15" height="15" border="0">
                      </a>
                      </td>
                      <td nowrap class="lablel1">
                      <a href="<%=response.encodeURL("perfData_apiSummary_jdbcAction.do?api="+apiName+"&type="+apiType+"&sortBy=count&sortDir=desc")%>">
                        <img src="common/graphics/sortDown.gif" width="15" height="15" border="0">
                      </a>
                      </td>
                    </tr>
                  </table></td>
              </tr>
              <%
                for (int j = 0; j < jdbcSummaries.length; j++)
                {
              %>
              <tr>
                <td width="33%" class="wrappedTD"><%=jdbcSummaries[j].getApiName()%></a></td>
                <td><%=ViewUtil.getFormattedTime(jdbcSummaries[j].getAverageTime())%></td>
                <td><%=jdbcSummaries[j].getCount()%></td>
              </tr>
              <%
                }
              %>
            </table>
      <!-- InstanceEndEditable --></td>
  </tr>
  <%
      if (sqlStatistics.length > 0)
      {
            int n = InfraREDWebOptions.getNoOfSqlQueriesToBeDisplayedInCallTracePage();
       %>
       <tr>
      <tr>
      <td class="lablel1Bold"> Top <%=n%> SQL queries by Average Execution Time </td>
      </tr>
      <%
        ArrayList topNQueriesByExecution = (ArrayList)session.getAttribute("sqlStatisticsByExec");
        request.setAttribute("sqlStatistics",topNQueriesByExecution);
        request.setAttribute("sqlTableName","topNByAvgExecTime");
        request.setAttribute("collapsedRowName","topNByAvgExecTimeCollpasedRow");

      %>
      <jsp:include page="includes/sqlStatistics.jsp" />
      <tr></tr>
      <tr>
      <td class="lablel1Bold"> Top <%=n%> SQL queries by Execution Count </td>
      </tr>
      <%
          ArrayList topNQueriesByCount = (ArrayList)session.getAttribute("sqlStatisticsByCount");
          request.setAttribute("sqlStatistics",topNQueriesByCount);
          request.setAttribute("sqlTableName","topNByAvgExecCount");
          request.setAttribute("collapsedRowName","topNByAvgExecCountCollpasedRow");
      %>
      <jsp:include page="includes/sqlStatistics.jsp" />
        </tr>
        <%
        }
        %>
</table>
</body>
<!-- InstanceEnd --></html>
