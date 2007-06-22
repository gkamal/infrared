package net.sf.infrared.tool.archive;

import java.io.File;
import java.io.IOException;

import net.sf.infrared.tool.Config;
import net.sf.infrared.tool.ConfigHolder;
import net.sf.infrared.tool.TestUtil;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestWebArchive {
	File workDir;
	File outputDir;
	
	@BeforeClass
	public static void setUpConfig(){
		String basedir = System.getProperty("user.dir")+"/target/";
		Config conf = ConfigHolder.getConfig();
		conf.setWorkDir(new File(basedir,"work-infrared"));
		conf.setOutputDir(new File(basedir,"output-infrared"));
	}
	
	@Before
	public void setDirs() throws IOException{
		workDir = ConfigHolder.getConfig().getWorkDir();
		outputDir = ConfigHolder.getConfig().getOutputDir();
		FileUtils.deleteDirectory(workDir);
		FileUtils.deleteDirectory(outputDir);
	}
	
	@Test
	public void explodedWar(){
		WebArchive web = new WebArchive(new File(TestUtil.getWarArchiveDir()+"wars/exploded"));
		web.initialize();
		assertTrue("Outputdir should contain the exploded archive",new File(outputDir,"exploded").exists());
		assertFalse("Nothing should be copied to the work directory in exploded",new File(workDir,"exploded").exists());
		web.cleanup();
	}
	
	
	/**
	 * In case of zipped war
	 * war->explode in work dir->integrate->zip and copy to output dir->delete the content in work dir
	 */
	@Test
	public void zippedWar(){
		WebArchive web = new WebArchive(new File(TestUtil.getWarArchiveDir()+"wars/zipped.war"));
		web.initialize();
		assertTrue(new File(workDir,"zipped").exists());
		web.integrate();
		web.cleanup();
		assertFalse(new File(workDir,"zipped").exists());
		assertTrue(new File(outputDir,"zipped.war").exists());

	}
	@Test
	public void zippedComplexNameWar(){
		WebArchive web = new WebArchive(new File(TestUtil.getWarArchiveDir()+"wars/zipped-0.3-SNAPSHOT.war"));
		web.initialize();
		assertTrue(new File(workDir,"zipped-0.3-SNAPSHOT").exists());
		web.integrate();
		web.cleanup();
		assertFalse(new File(workDir,"zipped-0.3-SNAPSHOT").exists());
		assertTrue(new File(outputDir,"zipped-0.3-SNAPSHOT.war").exists());
	}
	
	@Test
	public void testFileName() throws Exception {
		File f = new File(TestUtil.getWarArchiveDir()+"wars/zipped-0.3-SNAPSHOT.war");
		WebArchive arch = new WebArchive(f);
		assertEquals("zipped-0.3-SNAPSHOT",arch.getSrcFileName());
		
		//its a directory ending with a .war extension
		f = new File(TestUtil.getWarArchiveDir()+"wars/zipped-0.3.war");
		arch = new WebArchive(f);
		assertEquals("zipped-0.3.war",arch.getSrcFileName());

	}
	
	@After
	public  void deleteDirectories() throws IOException{
		Config conf = ConfigHolder.getConfig();
		FileUtils.deleteDirectory(conf.getWorkDir());
		FileUtils.deleteDirectory(conf.getOutputDir());
	}

}
