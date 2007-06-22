package net.sf.infrared.tool.archive;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import net.sf.infrared.tool.Archive;
import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.TestUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestArchiveFactory {
	ArchiveFactory factory = new ArchiveFactory();
	
	String param;
	ArchiveType value;
	
	public TestArchiveFactory(String param, ArchiveType value) {
		this.param = param;
		this.value = value;
	}

	@Parameters
	public static Collection data() {
		String basedir = TestUtil.getWarArchiveDir();
	    return Arrays.asList(new Object[][]{
	            {basedir+"wars/exploded",ArchiveType.WAR},
	            {basedir+"wars/exploded.war",ArchiveType.WAR},
	            {basedir+"wars/zipped.war",ArchiveType.WAR},
	    });
	}
	
	@Test
	public void testType() {
		File war = new File(param); 
		Archive a = factory.getArchive(war);
		assertEquals(a.getType(), value);
	}

}
