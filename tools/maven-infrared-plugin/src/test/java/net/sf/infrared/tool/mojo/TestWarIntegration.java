package net.sf.infrared.tool.mojo;

import static org.junit.Assert.fail;

import java.io.File;

import net.sf.infrared.tool.TestUtil;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class TestWarIntegration {
	protected InfraredMojo mojo;

	@Before
	public void setUp() {
		File baseDir = getBaseDir();
		mojo = new InfraredMojo();
		mojo.application = new File(baseDir, getApplicationName());
		mojo.basedir = baseDir;

		mojo.infraredConfDir = new File(baseDir + "/src/main/infrared");
		File target = new File(baseDir, "target");
		mojo.outputDir = new File(target, "output-infrared");
		mojo.workDir = new File(target, "work-infrared");
	}

	@Test
	public void integrateZippedWar() {
		try{
			mojo.execute();
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	protected File getBaseDir() {
		return new File(TestUtil.getProjectsDir(),getProjectName());
	}

	public void cleanUp() {
		try {
			FileUtils.deleteDirectory(new File(getBaseDir(), "target"));
		} catch (Exception ex) {
			;// Only a problem on windows. we really do not care.. if we cant
				// delete the file
			// It is probably not there
		}
	}

	protected String getApplicationName() {
		return "zipped-war.war";
	}

	protected String getProjectName() {
		return "zipped-war";
	}
}
