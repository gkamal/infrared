package net.sf.infrared.tool.integrator;


import static org.custommonkey.xmlunit.XMLAssert.*;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.TestUtil;
import net.sf.infrared.tool.util.XmlUtil;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TestWebXmlIntegrator {
	XmlUtil xmlUtil = new XmlUtil();
	File webDir;
	File tempWebxml;
	
	@Before
	public void setPaths() throws IOException{
		String web = TestUtil.getWebxmlDir();
		webDir = new File(web);
		
		tempWebxml = File.createTempFile("infraed-web", "xml");
	}

	@Test
	public void testCreateListenerNode() throws Exception {
		WebXmlIntegrator web = new WebXmlIntegrator();
		Document doc = createEmptyDocument();
		Node n = web.createListenerNode(doc);
		doc.appendChild(n);
		
		String expected ="<listener>"+
	      "<listener-class>"+WebXmlIntegrator.LISTENER_CLASS+"</listener-class>"+
	      "</listener>";
		Document expDoc = XMLUnit.buildControlDocument(expected);
		
		assertXMLEqual(expDoc,doc);
	}

	@Test
	public void testCreateFilterNode() throws Exception{
		WebXmlIntegrator web = new WebXmlIntegrator();
		Document doc = createEmptyDocument();
		Node n = web.createFilterNode(doc);
		doc.appendChild(n);
		
		String expected = " <filter><filter-name>infrared</filter-name>"+
						  "<filter-class>"+WebXmlIntegrator.FILTER_CLASS+"</filter-class>"+
			              "</filter>";
		Document expDoc = XMLUnit.buildControlDocument(expected);
		
		assertXMLEqual(expDoc,doc);
	}

	@Test
	public void testCreateFilterMappingNode() throws Exception {
		WebXmlIntegrator web = new WebXmlIntegrator();
		Document doc = createEmptyDocument();
		Node n = web.createFilterMappingNode(doc);
		doc.appendChild(n);
		
		String expected = "<filter-mapping><filter-name>infrared</filter-name><url-pattern>/*</url-pattern>" +
						  "</filter-mapping>";
		Document expDoc = XMLUnit.buildControlDocument(expected);
		
		assertXMLEqual(expDoc,doc);
	}
	
	@Test
	public void testEmptyWebxml() throws Exception {
		WebXmlIntegrator web = new WebXmlIntegrator(new File(webDir,"empty-web-xml"));
		web.setOutputWebXml(tempWebxml);
		
		web.integrate(ArchiveType.WAR);
		Document doc = xmlUtil.createDocument(tempWebxml);
		
		assertXPaths(doc);
	}
	
	@Test
	public void testLoadedWebxml() throws Exception {
		WebXmlIntegrator web = new WebXmlIntegrator(new File(webDir,"loaded-web-xml"));
		web.setOutputWebXml(tempWebxml);
		
		web.integrate(ArchiveType.WAR);
		Document doc = xmlUtil.createDocument(tempWebxml);
		
		assertXPaths(doc);
	}
	
	@Test
	public void testWeb25xml() throws Exception {
		WebXmlIntegrator web = new WebXmlIntegrator(new File(webDir,"web25-xml"));
		web.setOutputWebXml(tempWebxml);
		
		web.integrate(ArchiveType.WAR);
		Document doc = xmlUtil.createDocument(tempWebxml);
		
		assertXPaths(doc);
	}
	
	@After
	public void cleanDeleteTempFile() throws IOException{
		tempWebxml.delete();
	}

	//TODO Need to do DTD or Schema validation
	private void assertXPaths(Document doc) throws TransformerException {
		assertXpathEvaluatesTo("infrared", "//filter-mapping/filter-name",doc);
		assertXpathEvaluatesTo("infrared", "//filter/filter-name",doc);
		assertXpathEvaluatesTo(WebXmlIntegrator.FILTER_CLASS, "//filter/filter-class",doc);
		//Listner entry is the last one 
		assertXpathEvaluatesTo(WebXmlIntegrator.LISTENER_CLASS, "//listener[last()]/listener-class",doc);
	}
	
	Document createEmptyDocument() throws ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		DocumentBuilder parser = factory.newDocumentBuilder();
		return parser.newDocument();
	}

}
