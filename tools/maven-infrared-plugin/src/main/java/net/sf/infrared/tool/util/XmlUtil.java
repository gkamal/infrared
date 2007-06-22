package net.sf.infrared.tool.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.infrared.tool.InfraredToolException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public class XmlUtil {
	private static final Logger logger = Logger.getLogger(XmlUtil.class.getName());

	public Document createDocument(File file) {
		Document d = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			DocumentBuilder parser = factory.newDocumentBuilder();
			d = parser.parse(file);
		} catch (ParserConfigurationException e) {
			throw new InfraredToolException("Error in parsing xml document from " + file, e);
		} catch (IOException ioe) {
			throw new InfraredToolException("Error in opening xml document " + file, ioe);
		} catch (SAXException sae) {
			throw new InfraredToolException("Error in parsing xml document from " + file, sae);
		}
		return d;
	}

	public Node findFirstMatchingElement(Document doc, String[] names) {
		Node node = null;
		for (int i = 0; i < names.length; i++) {
			node = getFirstDescendantNodeByName(doc, names[i]);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

	public Node getFirstDescendantNodeByName(Document doc, String name) {
		NodeList nodes = doc.getElementsByTagName(name);
		if (nodes != null && nodes.getLength() != 0) {
			return nodes.item(0);
		} else {
			return null;
		}
	}

	public void writeDocumentToFile(Document doc, File file) {
		FileWriter writer = null;
		DOMImplementation implementation = null;
		boolean success = false;
		try {
			implementation = DOMImplementationRegistry.newInstance().getDOMImplementation("XML 3.0");
			if (implementation != null && implementation.getFeature("LS", "3.0") instanceof DOMImplementationLS) {
				DOMImplementationLS feature = (DOMImplementationLS) implementation.getFeature("LS", "3.0");
				LSSerializer serializer = feature.createLSSerializer();
				LSOutput output = feature.createLSOutput();
				try {
					writer = new FileWriter(file);
					output.setCharacterStream(writer);
					serializer.write(doc, output);
				} catch (IOException e) {
					logger.log(Level.WARNING, "Error in using the DOM3 serialization. reverting to xerces", e);
				} finally {
					closeSilently(writer);
				}
				success = true;
			} else {
				logger.log(Level.FINE, "DOM3 implementation not found");
			}
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error in using the DOM3 serialization. reverting to xerces", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error in using the DOM3 serialization. reverting to xerces", e);
		} catch (ClassCastException e) {
			logger.log(Level.WARNING, "Error in using the DOM3 serialization. reverting to xerces", e);
		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error in using the DOM3 serialization. reverting to xerces", e);
		}

		if (!success) {
			writeDocumentToFileUsingXerces(doc, file);
		}
	}

	private void writeDocumentToFileUsingXerces(Document doc, File file) {
		// TODO Should we have a dependency on xerces parser or use reflection
		// here
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			OutputFormat of = new OutputFormat(doc, "UTF-8", false);
			of.setIndenting(true);
			XMLSerializer xmlser = new XMLSerializer(writer, of);
			xmlser.serialize(doc);
			writer.flush();
		} catch (IOException ioex) {
			throw new InfraredToolException("Failed to write XML document " + doc + " to file "
					+ file.getAbsolutePath());
		} finally {
				closeSilently(writer);
		}

	}

	private void closeSilently(Closeable c) {
		try {
			c.close();
		} catch (IOException e) {
			// ignoring the closing exception here
			logger.log(Level.FINE, "Error in closing connection", e);
		}
	}

}
