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
package net.sf.infrared.tool;

import java.io.File;

/**
 * The configuration object used for integration. Used by various parts of
 * Infrared tool
 * @author chetanm
 * @date Jun 22, 2007
 * @version $Revision: 1.1 $
 */
public class Config {
	private static final String SEP = System.getProperty("line.separator");
	private static final String TAB = "...";
	private File workDir;
	private File outputDir;
	private File application;
	private File infraredConfDir;
	private ArchiveType initialArchiveType;

	public File getWorkDir() {
		return workDir;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public void setWorkDir(File workDir) {
		this.workDir = workDir;
	}

	public void setApplication(File app) {
		this.application = app;
	}

	public File getApplication() {
		return application;
	}

	public void setInfraredConfDir(File infraredConfDir) {
		this.infraredConfDir = infraredConfDir;
	}

	public void setInitialArchiveType(ArchiveType type) {
		this.initialArchiveType = type;
	}

	public File getInfraredConfDir() {
		return infraredConfDir;
	}

	public ArchiveType getInitialArchiveType() {
		return initialArchiveType;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		append(sb,"workDir",workDir);
		append(sb,"outputDir",outputDir);
		append(sb,"application",application);
		append(sb,"infraredConfDir",infraredConfDir);
		append(sb,"initialArchiveType",initialArchiveType);
		return sb.toString();
	}

	private void append(StringBuilder sb, String name, Object o) {
		sb.append(TAB).append(name).append(" = ").append(o).append(SEP);
	}
}
