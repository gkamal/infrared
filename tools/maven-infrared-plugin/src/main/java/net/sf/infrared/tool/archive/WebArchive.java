package net.sf.infrared.tool.archive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.infrared.tool.Archive;
import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.ConfigHolder;
import net.sf.infrared.tool.Integrator;
import net.sf.infrared.tool.integrator.AopXmlIntegrator;
import net.sf.infrared.tool.integrator.InfraredPropertiesIntegrator;
import net.sf.infrared.tool.integrator.WebXmlIntegrator;
import net.sf.infrared.tool.util.FileUtil;

import org.apache.commons.io.FilenameUtils;

/**
 * Depending on the web archive the flow would be decided
 * <ul>
 *    <li>Exploded - a -&gt; infrared-output/a
 *    <li>Zipped - a.war -&gt; infrared-work/a -&gt; infrared-output/a.war  
 *    <li>Exploded inside ear - To BE DECIDED
 * </ul>
 * Depending on above two properties the war processing would vary.
 * @author chetanm
 *
 */
public class WebArchive implements Archive {
	private static final Logger logger = Logger.getLogger(WebArchive.class.getName());
	private FileUtil  fileUtil = new FileUtil();
	
	private ArchiveState initialState;
	private Assembly assembly;

	private File src;
	private File workDir;
	private File outputDir;
	
	private List<Integrator> integrators = new ArrayList<Integrator>();
	
	public WebArchive(File src){
		this.src = src;
		initialState = (src.isDirectory()) ? ArchiveState.EXPLODED : ArchiveState.ZIPPED; 
	}
	
	public ArchiveType getType() {
		return ArchiveType.WAR;
	}

	public void initialize() {
		File rootWorkDir = ConfigHolder.getConfig().getWorkDir();
		workDir = new File(rootWorkDir,getSrcFileName());
		outputDir = ConfigHolder.getConfig().getOutputDir();
		
		File webapp = null;
		if (initialState ==  ArchiveState.EXPLODED){
			fileUtil.ensureDirectoryExists(outputDir);
			fileUtil.copyDirectory(src, outputDir);
			webapp = new File(outputDir,getSrcFileName());
		}else if (initialState == ArchiveState.ZIPPED){
			fileUtil.ensureDirectoryExists(workDir,outputDir);
			fileUtil.uncompressFileToDir(src, workDir);
			webapp = workDir;
		}
		logger.fine("Integrating webapp ["+getSrcFileName()+ "] \n workDir "+workDir+" outputDir "+outputDir);
		integrators.add(new WebXmlIntegrator(webapp));
		integrators.add(new AopXmlIntegrator(webapp));
		integrators.add(new InfraredPropertiesIntegrator(webapp));
		addIntegrators(integrators);
	}



	public void integrate() {
		for (Integrator i : integrators) {
			i.integrate(getType());
		}
	}
	
	public void cleanup() {
		if (initialState == ArchiveState.ZIPPED){
			fileUtil.compressDirToFile(workDir, new File(outputDir,src.getName()));
			fileUtil.deleteDirectory(workDir);
		}
	}
	
	/**
	 * Subclasses can override it to add more integrators if required
	 * @param i
	 */
	protected void addIntegrators(List<Integrator> i) {}
	
	String getSrcFileName() {
		String name = src.getName();
		if(src.isFile()){
			name = name.replaceAll("\\.war$", "");
			name = name.replaceAll("\\.zip$", "");
		}
		return name;
	}

}
