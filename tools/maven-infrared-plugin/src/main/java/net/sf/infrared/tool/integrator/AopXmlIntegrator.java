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
		
		File metainfdir = new File(app,"META-INF");
		fileUtil.ensureDirectoryExists(metainfdir);
		fileUtil.copyFileToDirectory(aopxml,metainfdir);
		logger.fine("Copied aop.xml to "+metainfdir);
		
		//TODO what if there already exist aop.xml. Should we merge them
	}

}
