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
 * Original Author:  chetan.mehrotra (Tavant Technologies)
 * Contributor(s):   -;
 *
 */

package net.sf.infrared.web.util.sql;

import java.io.IOException;
import java.io.StringReader;

import net.sf.infrared.base.util.LoggingFactory;

import org.apache.log4j.Logger;

/**
 * Most of the code in this package is taken from <a href="http://ostermiller.org/syntax/">syntax
 * highlighting</a> package. The only change that is done is the SQLLexer has been changed 
 * to allow double quotes for enclosing strings. For this the regular expression
 * for string has been tweaked.
 * 
 * Also a new categaroy of quoted strings is added
 * 
 * This package also uses <a href="http://home.comcast.net/~danmeany/sqlformatter.html">
 * SQLFormatter</a> for SQL formatting.
 * 
 * 
 * @author chetan.mehrotra
 * @date Dec 15, 2005
 * @version $Revision: 1.2 $ 
 */
public class SQLToHtml {

	private static final Logger logger = LoggingFactory.getLogger(SQLToHtml.class.getName());
	private static final String SPAN_OPEN = "<span class=";
	private static final String SPAN_CLOSE = "</span>";
	private static final String GT = ">";
	
	public static String convertToHtml(String sql){
		SQLFormatter formatter = new SQLFormatter();
		formatter.setText(sql);
		formatter.format();
		sql = formatter.getText();
		String formattedString = null;
		try {
			formattedString = parseSql(sql);
		} catch (IOException e) {
			formattedString = sql;
			logger.error("Not able to parse the sql", e);
		}
		return formattedString;
	}

	public static String parseSql(String sql) throws IOException {
		StringReader reader = new StringReader(sql);
		Lexer sqlLexer = new SQLLexer(reader);
		
		StringBuffer sb = new StringBuffer();
		String currentDescription = null;
		Token token;
		sb.append("<pre>");
		while((token = sqlLexer.getNextToken()) != null){
            // optimization implemented here:
            // ignored white space can be put in the same span as the stuff
            // around it.  This saves space because spans don't have to be
            // opened and closed.            
            if (token.isWhiteSpace() ||
                   (currentDescription != null && token.getDescription().equals(currentDescription))){
                writeEscapedHTML(token.getContents(), sb);
            } else {
                if (currentDescription != null) closeSpan(sb);
                currentDescription = token.getDescription();
                openSpan(currentDescription, sb);
                writeEscapedHTML(token.getContents(), sb);
            }         
        }
        if (currentDescription != null) closeSpan(sb);
        sb.append("</pre>");
		return sb.toString();
	}
	
	private static void openSpan(String description,StringBuffer sb){
		sb.append(SPAN_OPEN).append(description).append(GT);
	}
	
	private static void closeSpan(StringBuffer sb){
		sb.append(SPAN_CLOSE);
	}
	
	/**
	 * Write the string after escaping characters that would hinder 
	 * it from rendering in html.
	 * 
	 * @param text The string to be escaped and written
	 * @param out output gets written here
	 */
	public static void writeEscapedHTML(String text, StringBuffer sb){
		for (int i=0; i < text.length(); i++){
			char ch = text.charAt(i);
            switch(ch){
                case '<': {
                	sb.append("&lt;");
                    break;
                }
                case '>': {
                	sb.append("&gt;");
                    break;
                }
                case '&': {
                	sb.append("&amp;");
                    break;
                }
                case '"': {
                	sb.append("&quot;");
                    break;
                }
                default: {
                	sb.append(ch);
                    break;
                }
            }
		}
	}

}
