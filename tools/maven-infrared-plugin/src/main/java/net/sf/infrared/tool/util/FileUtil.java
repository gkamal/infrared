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
package net.sf.infrared.tool.util;

import java.io.File;
import java.io.IOException;

import net.sf.infrared.tool.InfraredToolException;

import org.apache.commons.io.FileUtils;

/**
 * Common utility methods related to file handling
 * @author chetanm
 * @date Jun 22, 2007
 * @version $Revision: 1.1 $
 */
public class FileUtil {
	
	private JarHelper jarHelper = new JarHelper();

	public void uncompressFileToDir(File srcFile,File outputDir) {
		try {
			jarHelper.unjarDir(srcFile, outputDir);
		} catch (IOException ex) {
			throw new InfraredToolException("Failed to unarchive " + srcFile.getAbsolutePath() + " to directory "
					+ outputDir.getAbsolutePath(), ex);
		}
	}

	public void copyDirectory(File srcDir,File destDir) {
		try {
			FileUtils.copyDirectoryToDirectory(srcDir, destDir);
		} catch (IOException ex) {
			throw new InfraredToolException("Failed to copy directory " + srcDir.getAbsolutePath() + " to directory "
					+ destDir.getAbsolutePath(), ex);
		}
	}

	public void compressDirToFile(File sourceDir,File jarFile) {
		try {
			jarHelper.jarDir(sourceDir, jarFile);
		} catch (IOException ex) {
			throw new InfraredToolException("Failed to archive " + sourceDir.getAbsolutePath() + " to "
					+ jarFile.getAbsolutePath(), ex);
		}
	}

	public void deleteDirectory(File dir) {
		try {
			FileUtils.deleteDirectory(dir);
		} catch (IOException ex) {
			throw new InfraredToolException("Failed to delete directory " + dir, ex);
		}
		
	}
	
	public void ensureDirectoryExists(File ... dirs){
		for(File directory : dirs){
			if (directory.exists()) {
	            if (directory.isFile()) {
	                String message =
	                    "File " + directory
	                            + " exists and is not a directory. Unable to create directory.";
	                throw new InfraredToolException(message);
	            }
	        } else {
	            if (!directory.mkdirs()) {
	                throw new InfraredToolException("Unable to create directory " + directory);
	            }
	        }
		}
	}
	
	public boolean containsFile(File jarFile, String entryName) {
		try {
			return jarHelper.containsFile(jarFile, entryName,true);
		} catch (IOException e) {
			throw new InfraredToolException("Error ocured while determining file entry",e);
		}
	}

	public void copyFileToDirectory(File file, File dir) {
		try {
			FileUtils.copyFileToDirectory(file,dir);
		} catch (IOException ex) {
			throw new InfraredToolException("Failed to copy file " + file+ "to directory"+dir, ex);
		}
		
	}
	

}
