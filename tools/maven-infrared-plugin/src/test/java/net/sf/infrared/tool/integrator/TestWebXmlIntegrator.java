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
package net.sf.infrared.tool.integrator;


import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.infrared.tool.ArchiveType;
import net.sf.infrared.tool.TestUtil;
import net.sf.infrared.tool.util.XmlUtil;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 
 * @author chetanm
 *
 */
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
		printToSysout(doc);
		assertXPaths(doc);
	}
	
	@Ignore("The xpath is not matching because of namespace. Find an alternate way")
	@Test
	public void testWeb25xml() throws Exception {
		WebXmlIntegrator web = new WebXmlIntegrator(new File(webDir,"web25-xml"));
		web.setOutputWebXml(tempWebxml);
		
		web.integrate(ArchiveType.WAR);
		Document doc = xmlUtil.createDocument(tempWebxml);
		
		//printToSysout(doc);
		//The test would fail as the web.xml 2.5 onwardshave a default namespace instead of DTD
		//Now xmlunit 1.0 does not have any support for that 1.1 beta2 has
		//So would look into that else would have to evaluate manually
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
	
	/**
	 * For debugging purpose
	 * @param doc
	 * @throws Exception
	 */
	private void printToSysout(Document doc) throws Exception{
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(System.out);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.transform(source, result);
	}

}
