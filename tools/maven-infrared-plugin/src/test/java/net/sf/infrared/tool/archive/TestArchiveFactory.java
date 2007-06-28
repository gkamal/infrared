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
/**
 * @author chetanm
 */
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
