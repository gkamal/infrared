package net.sf.infrared.tool.integrator;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.ConfigHolder;
import net.sf.infrared.tool.Integrator;
import net.sf.infrared.tool.util.FileUtil;

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
