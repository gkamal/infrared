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
 */
package net.sf.infrared.tool;

/**
 * Base exception used to wrap up any checked exception occuring in the Infrared integration.
 * @author chetanm
 * @date Jun 22, 2007
 * @version $Revision: 1.1 $
 */
public class InfraredToolException extends RuntimeException {
	private String infraredExpMessage="";
	public InfraredToolException() {
	}

	public InfraredToolException(String message) {
		super(message);
		this.infraredExpMessage = message;
	}

	public InfraredToolException(Throwable cause) {
		super(cause);
	}

	public InfraredToolException(String message, Throwable cause) {
		super(message, cause);
		this.infraredExpMessage = message;
	}
	
	public String getInfraredMessage(){
		return this.infraredExpMessage;
	}

}
