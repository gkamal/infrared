package net.sf.infrared.tool;

/**
 * Performs the specific integration operation required for Infrared
 * @author chetanm
 *
 */
public interface Integrator {
	
	/**
	 * It would be called once the app is in exploded form. The integrator can decide the process
	 * depending on the archive type.
	 *  
	 * @param type type of the application
	 */
	void integrate(ArchiveType type);
	
}
