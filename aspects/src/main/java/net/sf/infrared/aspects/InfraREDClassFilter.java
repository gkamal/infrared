package net.sf.infrared.aspects;

import java.io.Serializable;

import org.springframework.aop.ClassFilter;

public class InfraREDClassFilter implements ClassFilter, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5699418843889453552L;
	private String[] rootPackages = new String[]{};
	private String[] classExcluded = new String[]{};
	private String[] subPackageExcluded = new String[]{};
	
	public boolean matches(Class clazz) {
		Class[] interfaces = clazz.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (matchDomain(interfaces[i])) {
				return true;
			}
		}

		if (matchDomain(clazz)) {
			return true;
		}
		return false;
	}

	private boolean matchDomain(Class clazz) {
		String className = clazz.getName();
		boolean result = false;

		for (int i = 0; i < rootPackages.length; i++) {
			if (className.startsWith(rootPackages[i])) {
				result = true;
				break;
			}
		}
		// Class does not belong to allowed package so return false
		if (!result) {
			return false;
		}
		// Check whether class belong to exclded class list
		for (int i = 0; i < classExcluded.length; i++) {
			if (className.endsWith(classExcluded[i])) {
				return false;
			}
		}
		// Check wether class belong to any excluded package
		for (int i = 0; i < subPackageExcluded.length; i++) {
			if (className.indexOf("." + subPackageExcluded[i] + ".") != -1) {
				return false;
			}
		}
		return result;
	}

	/**
	 * @return Returns the classExcluded.
	 */
	public String[] getClassExcluded() {
		return classExcluded;
	}

	/**
	 * @param classExcluded
	 *            The classExcluded to set.
	 */
	public void setClassExcluded(String[] classExcluded) {
		this.classExcluded = classExcluded;
	}

	/**
	 * @return Returns the subPackageExcluded.
	 */
	public String[] getSubPackageExcluded() {
		return subPackageExcluded;
	}

	/**
	 * @param subPackageExcluded
	 *            The subPackageExcluded to set.
	 */
	public void setSubPackageExcluded(String[] subPackageExcluded) {
		this.subPackageExcluded = subPackageExcluded;
	}

	/**
	 * @return Returns the rootPackages.
	 */
	public String[] getRootPackages() {
		return rootPackages;
	}

	/**
	 * @param rootPackages
	 *            The rootPackages to set.
	 */
	public void setRootPackages(String[] rootDomains) {
		this.rootPackages = rootDomains;
	}
}
