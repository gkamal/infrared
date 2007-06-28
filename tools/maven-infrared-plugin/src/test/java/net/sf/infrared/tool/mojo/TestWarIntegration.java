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
package net.sf.infrared.tool.mojo;

import static org.junit.Assert.fail;

import java.io.File;

import net.sf.infrared.tool.TestUtil;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author chetanm
 */
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

	@After
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
