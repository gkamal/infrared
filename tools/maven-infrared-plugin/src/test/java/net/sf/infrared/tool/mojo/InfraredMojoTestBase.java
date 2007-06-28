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

import java.io.File;

import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.project.MavenProject;

/**
 * 
 * @author chetanm
 *
 */
public  class InfraredMojoTestBase {
	
	void installPlugin() throws Exception{
		MavenEmbedder embedder = new MavenEmbedder();
		embedder.setClassLoader(Thread.currentThread().getContextClassLoader());
		try{
			embedder.start();
			MavenProject project = embedder.readProject(new File(getProjectDir(),"pom.xml"));
		}finally {
			//embedder.stop();
		}
	}

	private File getProjectDir() {
		String base = System.getProperty("user.dir");
		return new File(base);
	}
	
	public static void main(String[] args) throws Exception {
		InfraredMojoTestBase b = new InfraredMojoTestBase();
		b.installPlugin();
	}

}
