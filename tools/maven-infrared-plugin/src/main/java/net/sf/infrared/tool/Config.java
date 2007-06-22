package net.sf.infrared.tool;

import java.io.File;

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
