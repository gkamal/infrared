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
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 *
-->

<%@ page import="net.sf.infrared.web.util.ViewUtil,
                 net.sf.infrared.web.util.PerformanceDataSnapshot,
                 net.sf.infrared.web.formbean.SqlStatisticsFormBean"%>

<%@ taglib uri="struts/bean" prefix="bean" %>
<%@ taglib uri="struts/logic" prefix="logic" %>
<%@ taglib uri="struts/html" prefix="html" %>
<head>
<script>
funtion showSql(sqlid)
{
  window.open('showSql.jsp?name=sqlname','SqlQuery','height=200,width=400,location=no,resizable=yes,scrollbars=yes,toolbars=no');
}
</script>
</head>
<%
    perfData = (PerformanceDataSnapshot)session.getAttribute("perfData");
    sqlTableName = (String) request.getAttribute("sqlTableName");
    collapsedRowName = (String) request.getAttribute("collapsedRowName");
%>
    <tr id="<%=collapsedRowName%>">
	        <td class="lablel1Bold" align="left" valign="middle">
                <img src="fw/def/image/tree/15/32.gif"
                     onClick="showHide('<%=sqlTableName%>','<%=collapsedRowName%>')">
            SQL Statistics
            </td>
        </tr>
        <script>
            document.getElementById('<%=collapsedRowName%>').style.display='none';
        </script>

      <tr id="<%=sqlTableName%>">
          <td class="noBorderCell">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td   class="lablel1Bold" width="30%" rowspan="2" >
                    <img src="fw/def/image/tree/15/16.gif"
                         onClick="showHide('<%=sqlTableName%>','<%=collapsedRowName%>')">
                        Sql Query</td>
                <td   class="lablel1Bold" colspan="3">Avg. Time(ms)</td>
                <td   class="lablel1Bold" colspan="2">Count</td>
                <td   class="lablel1Bold" colspan="2">Max. Time(ms)</td>
                <td   class="lablel1Bold" colspan="2">Min. Time(ms)</td>
                <td   class="lablel1Bold" colspan="2">First Exec.Time(ms)</td>
                <td   class="lablel1Bold" colspan="2">Last Exec.Time(ms)</td>
              </tr>
              <tr>
                <td   class="lablel1Bold">Total</td>
                <td   class="lablel1Bold">Execute</td>
                <td   class="lablel1Bold">Prepare</td>
                <td   class="lablel1Bold">Execute</td>
                <td   class="lablel1Bold">Prepare</td>
                <td   class="lablel1Bold">Execute</td>
                <td   class="lablel1Bold">Prepare</td>
                <td   class="lablel1Bold">Execute</td>
                <td   class="lablel1Bold">Prepare</td>
                <td   class="lablel1Bold">Execute</td>
                <td   class="lablel1Bold">Prepare</td>
                <td   class="lablel1Bold">Execute</td>
                <td   class="lablel1Bold">Prepare</td>
              </tr>
              <tr>
                    <logic:iterate id="sqlStats" collection="<%= request.getAttribute(\"sqlStatistics\") %>">

                    <td class="wrappedTD <bean:write name="sqlStats" property="tdStyle"/>" width="30%">
                        <%
                            String sql = ((SqlStatisticsFormBean)sqlStats).getSql();
                            if (sql.length() > 25) {
                         %>
                         <a href="javascript:;" onClick="window.open('showSql.jsp?name=<%=sql%>','SqlQuery','height=200,width=400,location=no,resizable=yes,scrollbars=yes,toolbars=no')"> 
							<%=sql.substring(0,25)+"..."%>
						</a>
                         <%
                            }
                            else {
                         %>
                            <%=sql%>
                         <%
                            }
                         %>

                    </td>

                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="avgTotalTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="avgExecuteTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="avgPrepareTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="noOfExecutes"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="noOfPrepares"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="maxExecuteTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="maxPrepareTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="minExecuteTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="minPrepareTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="lastExecuteTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="lastPrepareTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="firstExecuteTime"/></td>
                    <td class="numericTD <bean:write name="sqlStats" property="tdStyle"/>"><bean:write name="sqlStats" property="firstPrepareTime"/></td>
               </tr>

                    </logic:iterate>


            </table></td>
        </tr>
