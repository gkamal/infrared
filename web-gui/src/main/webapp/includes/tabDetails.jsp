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

    <%
        String highlight = (String)request.getAttribute("highlight");
    	String activeMode = (String)session.getAttribute("isInActiveMode");
        String [] tds = { "<td nowrap class=\"menuUp\">",
                          "<td nowrap class=\"menuUp\">",
                          "<td nowrap class=\"menuUp\">",
                          "<td nowrap class=\"menuUp\">",
                          "<td nowrap class=\"menuUp\">"
                        };
        if(highlight.equals("summary"))
            tds[0] = "<td nowrap class=\"menuDown\">";
        else if(highlight.equals("lastInv"))
            tds[1] = "<td nowrap class=\"menuDown\">";
        else if(highlight.equals("configuration"))
            tds[2] = "<td nowrap class=\"menuDown\">";
        else if(highlight.equals("help"))
            tds[3] = "<td nowrap class=\"menuDown\">";

    %>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="100%" nowrap class="menuUp">&nbsp;</td>
          <%= tds[0] %><a href="<%=response.encodeURL("perfData_summaryAction.do")%>" Class="menu">Performance Summary</a></td>
          <%
          		if(activeMode.equals("true"))
          		{
          %>
          <%= tds[1] %><a href="<%=response.encodeURL("perfData_lastInvTrees.do")%>" class="menu">Last Invocations</a></td>
          
          <%
          		}
          %>
          <%= tds[2] %><a href="<%=response.encodeURL("configureAction.do")%>" class="menu">Configure</a></td>
          <% if(highlight.equals("summary"))
              {
          %>
                <td nowrap class="menuUp"><a href="<%=response.encodeURL("exportToExcel.do")%>" class="menu">Export To Excel</a></td>
          <%
              }
          %>
          <%= tds[3] %><a href="<%=response.encodeURL("help.jsp")%>" class="menu">Help</a></td>
        </tr>
      </table>
