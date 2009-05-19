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
import java.io.Reader;

/**
 * Interface used by the lexer.
 * @author chetan.mehrotra
 * @date Dec 15, 2005
 * @version $Revision: 1.2 $ 
 */
public interface Lexer {

	Token getNextToken() throws IOException;
	 
	void reset(Reader reader,int yyline, int yychar, int yycolumn) throws IOException;
}
