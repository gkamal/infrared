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
package net.sf.infrared.tool.integrator;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.ConfigHolder;
import net.sf.infrared.tool.Integrator;
import net.sf.infrared.tool.util.FileUtil;

/**
 * It copies the user specified aop.xml to the application/META-INF directory of the application.
 * It can be used for both web or ear.
 * 
 * @author chetanm
 * @date Jun 22, 2007
 * @version $Revision: 1.1 $
 */
public class AopXmlIntegrator implements Integrator {
	private static final Logger logger = Logger.getLogger(AopXmlIntegrator.class.getName());
	private File app;
	private FileUtil fileUtil = new FileUtil();
	
	public AopXmlIntegrator(File app){
		this.app = app;
	}
	
	public void integrate(ArchiveType type) {
		File confDir = ConfigHolder.getConfig().getInfraredConfDir();
		File aopxml = new File(confDir,"aop.xml");
		if(!aopxml.exists()){
			logger.log(Level.FINE,"aop.xml not found in ["+confDir+"] so would not be copied");
			return;
		}
		
		// Tomcat does not recognize war/META-INF/aop.xml for that put that in 
		// WEB-INF/aop.xml or WEB-INF/classes/META-INF/aop.xml.
		// For the rest copy that to META-INF/aop.xml
		File toDir = null;
		if (type == ArchiveType.WAR){
			toDir = new File(app,"WEB-INF");
		}else{
			toDir = new File(app,"META-INF");
		}
		
		fileUtil.ensureDirectoryExists(toDir);
		fileUtil.copyFileToDirectory(aopxml,toDir);
		logger.fine("Copied aop.xml to "+toDir);
		
		//TODO what if there already exist aop.xml. Should we merge them
	}

}
