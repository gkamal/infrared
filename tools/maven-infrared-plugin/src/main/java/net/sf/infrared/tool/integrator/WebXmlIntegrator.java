package net.sf.infrared.tool.integrator;

import java.io.File;
import java.util.logging.Logger;

import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.Integrator;
import net.sf.infrared.tool.util.XmlUtil;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class WebXmlIntegrator implements Integrator {
	private static final Logger logger = Logger.getLogger(WebXmlIntegrator.class.getName());
	public static final String FILTER_CLASS = "net.sf.infrared.aspects.servlet.InfraREDServletFilter";
	public static final String LISTENER_CLASS = "net.sf.infrared.agent.setup.InfraREDServletContextListener";
	private XmlUtil xmlUtil = new XmlUtil();

	private File webapp;

	private File webxml;
	
	/**
	 * By default it is same as webxml. But we might change it during testing so that
	 * original does not get modified
	 */
	private File outputWebxml;
	
	/**
	 * Package visible for test cases
	 */
	WebXmlIntegrator(){}

	public WebXmlIntegrator(File webapp) {
		this.webapp = webapp;
		File webinf = new File(webapp, "WEB-INF");
		this.webxml = new File(webinf, "web.xml");
		this.outputWebxml = this.webxml;
	}

	public void integrate(ArchiveType type) {
		Document webDoc = xmlUtil.createDocument(webxml);
		Node webAppNode = webDoc.getElementsByTagName("web-app").item(0);
		if (webAppNode == null) {
			throw new IllegalArgumentException("WEB-INF/web.xml does not contain <web-app> element");
		}
		// TODO Listner should be added only if webapp is independent and not
		// part of any ear
		addListenerNode(webDoc, webAppNode);
		addFilterNode(webDoc, webAppNode);
		addFilterMappingNode(webDoc, webAppNode);
		xmlUtil.writeDocumentToFile(webDoc, outputWebxml);
		logger.fine("Modified web.xml");
	}

	void addListenerNode(final Document doc, final Node webAppNode) throws DOMException {
		// adding <listener>.
		Node listenerNode = createListenerNode(doc);
		Node refNode = findNodeAfterListener(doc);
		// find the first node after <listener> and insert before it. Here if multiple listeners are
		//there then it would be the last one
		// if no node after <listener>, insert at the end of the document
		if (refNode != null) {
			webAppNode.insertBefore(listenerNode, refNode);
		} else {
			webAppNode.appendChild(listenerNode);
		}
	}

	Node findNodeAfterListener(Document doc) {
		// possible nodes after <listener>
		String[] possibleChildren = new String[] { "servlet", "servlet-mapping", "session-config", "mime-mapping",
				"welcome-file-list", "error-page", "taglib", "resource-env-ref", "resource-ref", "security-constraint",
				"login-config", "security-role", "env-entry", "ejb-ref", "ejb-local-ref" };
		return xmlUtil.findFirstMatchingElement(doc, possibleChildren);
	}

	// <web-app>
	// ..
	// <listener>
	// <listener-class>net.sf.infrared.agent.setup.InfraREDServletContextListener</listener-class>
	// </listener>
	// ..
	// </web-app>
	Node createListenerNode(Document doc) {
		Node listenerNode = doc.createElement("listener");
		Node listenerClassNode = doc.createElement("listener-class");
		listenerClassNode.appendChild(doc.createTextNode(LISTENER_CLASS));
		listenerNode.appendChild(listenerClassNode);
		return listenerNode;
	}

	void addFilterNode(final Document doc, final Node webAppNode) throws DOMException {
		// adding <filter>. This is added as the first filter, right after
		// <context-param>
		Node filterNode = createFilterNode(doc);
		Node refNode = findNodeAfterContextParam(doc);
		// find the first node after <context-param> and insert before it
		// if no node after <context-param>, insert at the end of the document
		if (refNode != null) {
			webAppNode.insertBefore(filterNode, refNode);
		} else {
			webAppNode.appendChild(filterNode);
		}
	}

	// <web-app>
	// ..
	// <filter>
	// <filter-name>infrared</filter-name>
	// <filter-class>net.sf.infrared.aspects.servlet.InfraREDServletFilter</filter-class>
	// </filter>
	// ..
	// </web-app>
	Node createFilterNode(Document doc) {
		Node filterNode = doc.createElement("filter");
		Node filterNameNode = doc.createElement("filter-name");
		filterNameNode.appendChild(doc.createTextNode("infrared"));
		Node filterClassNode = doc.createElement("filter-class");
		filterClassNode.appendChild(doc.createTextNode(FILTER_CLASS));
		filterNode.appendChild(filterNameNode);
		filterNode.appendChild(filterClassNode);
		return filterNode;
	}

	Node findNodeAfterContextParam(Document doc) {
		// possible nodes after <context-param>
		String[] possibleChildren = new String[] { "filter", "filter-mapping", "listener", "servlet",
				"servlet-mapping", "session-config", "mime-mapping", "welcome-file-list", "error-page", "taglib",
				"resource-env-ref", "resource-ref", "security-constraint", "login-config", "security-role",
				"env-entry", "ejb-ref", "ejb-local-ref" };
		return xmlUtil.findFirstMatchingElement(doc, possibleChildren);
	}

	void addFilterMappingNode(final Document doc, final Node webAppNode) throws DOMException {
		// adding <filter-mapping>. This is added as the first mapping, right
		// after <filter>
		Node filterMappingNode = createFilterMappingNode(doc);
		Node refNode = findNodeAfterFilter(doc);
		// find the first node after <filter> and insert before it
		// if no node after <filter>, insert at the end of the document
		if (refNode != null) {
			webAppNode.insertBefore(filterMappingNode, refNode);
		} else {
			webAppNode.appendChild(filterMappingNode);
		}
	}

	// <web-app>
	// ..
	// <filter-mapping>
	// <filter-name>infrared</filter-name>
	// <url-pattern>/*</url-pattern>
	// </filter-mapping>
	// ..
	// </web-app>
	Node createFilterMappingNode(Document doc) {
		Node filterMappingNode = doc.createElement("filter-mapping");
		Node filterNameNode = doc.createElement("filter-name");
		filterNameNode.appendChild(doc.createTextNode("infrared"));
		Node urlPatternNode = doc.createElement("url-pattern");
		urlPatternNode.appendChild(doc.createTextNode("/*"));
		filterMappingNode.appendChild(filterNameNode);
		filterMappingNode.appendChild(urlPatternNode);
		return filterMappingNode;
	}

	Node findNodeAfterFilter(Document doc) {
		// possible nodes after <filter>
		String[] possibleChildren = new String[] { "filter-mapping", "listener", "servlet", "servlet-mapping",
				"session-config", "mime-mapping", "welcome-file-list", "error-page", "taglib", "resource-env-ref",
				"resource-ref", "security-constraint", "login-config", "security-role", "env-entry", "ejb-ref",
				"ejb-local-ref" };
		return xmlUtil.findFirstMatchingElement(doc, possibleChildren);
	}
	
	void setOutputWebXml(File xml){
		this.outputWebxml = xml;
	}

}
