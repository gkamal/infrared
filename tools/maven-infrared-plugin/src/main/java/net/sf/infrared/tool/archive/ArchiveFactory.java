package net.sf.infrared.tool.archive;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.sf.infrared.tool.Archive;
import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.InfraredToolException;
import net.sf.infrared.tool.util.FileUtil;
import net.sf.infrared.tool.util.JarHelper;

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
