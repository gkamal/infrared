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
 * @author chetanm
 * @date Jun 22, 2007
 * @version $Revision: 1.1 $
 */
public class InfraredPropertiesIntegrator implements Integrator {
	private static final Logger logger = Logger.getLogger(InfraredPropertiesIntegrator.class.getName());
	private FileUtil fileUtil = new FileUtil();
	private File app;
	
	public InfraredPropertiesIntegrator(File app) {
		this.app = app;
	}

	public void integrate(ArchiveType type) {
		File confDir = ConfigHolder.getConfig().getInfraredConfDir();
		File props = new File(confDir,"infrared-agent.properties");
		if(!props.exists()){
			logger.log(Level.FINE,"infrared-agent.properties not found in ["+confDir+"] so would not be copied");
			return;
		}
		if(type == ArchiveType.WAR){
			copyToWar(props);
		}
		
	}

	private void copyToWar(File props) {
		File webinf = new File(app,"WEB-INF");
		File webinfclasses = new File(webinf,"classes");
		fileUtil.ensureDirectoryExists(webinfclasses);
		fileUtil.copyFileToDirectory(props, webinfclasses);
		logger.log(Level.FINE,"infrared-agent.properties copied to ["+webinfclasses+"]");
	}

}
