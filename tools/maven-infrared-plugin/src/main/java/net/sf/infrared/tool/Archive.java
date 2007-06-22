package net.sf.infrared.tool;

public interface Archive {
	enum ArchiveState { EXPLODED,ZIPPED};
	enum Assembly {INDEPENDENT,PART_OF};
	
	ArchiveType getType();
	
	void initialize();
	
	void integrate();
	
	void cleanup();
	
}
