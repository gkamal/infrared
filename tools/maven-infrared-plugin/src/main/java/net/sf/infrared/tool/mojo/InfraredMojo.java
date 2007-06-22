package net.sf.infrared.tool.mojo;

import java.io.File;
import java.util.logging.Handler;

import net.sf.infrared.tool.Archive;
import net.sf.infrared.tool.Config;
import net.sf.infrared.tool.ConfigHolder;
import net.sf.infrared.tool.InfraredToolException;
import net.sf.infrared.tool.archive.ArchiveFactory;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Modifies the archive (war,ear etc) for use with Infrared.
 * @goal integrate
 * @description Infrared integrator plugin
 * @requiresDirectInvocation false
 * @author chetanm
 */
public class InfraredMojo extends AbstractMojo {
	
    /**
     * The basedir of the project.
     * 
     * @parameter expression="${basedir}"
     * @required @readonly
     */
    protected File basedir;
    
    /**
     * The temporary directory to use for the application.
     * Defaults to target/work-infrared
     * 
     * @parameter expression="${project.build.directory}/work-infrared"
     * @required
     */
    protected File workDir;
    
    /**
     * The temporary directory to use for the application.
     * Defaults to target/output-infrared
     * 
     * @parameter expression="${project.build.directory}/output-infrared"
     * @required
     */
    protected File outputDir;
    
    /**
     * The location of the archive file.
     * @parameter expression="${project.build.directory}/${project.build.finalName}"
     * @required
     */
    protected File application;
    
    /**
     * Log level for the messages. Allowed values are quiet,debug,verbose
     * @parameter default-value="debug"
     */
    protected String logLevel;
    
    /**
     * The source directory for the Infrared configuration. Defaults to 
     * ${basedir}/src/main/infrared
     * @parameter expression="${basedir}/src/main/infrared"
     */
    protected File infraredConfDir;
    
    protected ArchiveFactory archiveFactory = new ArchiveFactory();

	public void execute() throws MojoExecutionException, MojoFailureException {
		Handler h = new MavenMessageHandler(getLog(),logLevel);
		LoggingConfigurer.configure(h);
		
		Config conf = ConfigHolder.getConfig();
		configure(conf);
		try{
			Archive arch = archiveFactory.getArchive(application);
			conf.setInitialArchiveType(arch.getType());
			arch.initialize();
			arch.integrate();
			arch.cleanup();
		}catch(InfraredToolException e){
			getLog().debug(conf.toString());
			if (e.getCause() != null)
				throw new MojoExecutionException(e.getInfraredMessage(),e.getCause());
			else
				throw new MojoExecutionException(e.getInfraredMessage());
		}
		
		LoggingConfigurer.remove(h);
	}

	protected void configure(Config conf) {
		conf.setOutputDir(outputDir);
		conf.setWorkDir(workDir);
		conf.setApplication(application);
		conf.setInfraredConfDir(infraredConfDir);
	}

}
