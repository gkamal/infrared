
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

import java.io.File;
import java.util.logging.Logger;

import net.sf.infrared.tool.Archive;
import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.InfraredToolException;
import net.sf.infrared.tool.util.FileUtil;

/**
 * Creates the appropriate archive type depending on the type of archive as
 * determined by file contents
 * @author chetanm
 * @date Jun 22, 2007
 * @version $Revision: 1.1 $
 */
public class ArchiveFactory {
	private static final Logger logger = Logger.getLogger(ArchiveFactory.class
			.getName());
	
	private FileUtil fileUtil = new FileUtil();

	public Archive getArchive(File appFile) {
		Archive a = null;
		ArchiveType type = getType(appFile);
		switch (type) {
		case WAR:
			a = new WebArchive(appFile);
			break;
		}
		return a;
	}

	ArchiveType getType(File file) {
		if (file.isDirectory()) {
			if (new File(file, "WEB-INF").exists()) {
				return ArchiveType.WAR;
			}
		}else{
			if (fileUtil.containsFile(file,"WEB-INF/")){
				return ArchiveType.WAR;
			}
		}
		throw new InfraredToolException("Cannot determine archive type for the file "+file);
	}



}
