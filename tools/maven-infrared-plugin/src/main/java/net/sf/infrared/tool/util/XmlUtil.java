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

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.infrared.tool.InfraredToolException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Common utility methods related to xml 
 * @author chetanm
 * @date Jun 22, 2007
 * @version $Revision: 1.1 $
 */
public class XmlUtil {
	private static final Logger logger = Logger.getLogger(XmlUtil.class.getName());
	private static final EntityResolver DUMMY_RESOLVER = new DummyResolver();

	public Document createDocument(File file) {
		Document d = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			DocumentBuilder parser = factory.newDocumentBuilder();
			parser.setEntityResolver(DUMMY_RESOLVER);
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
		OutputStream os = null;
		try {
			DOMSource source = new DOMSource(doc);
			os = new BufferedOutputStream(new FileOutputStream(file));
			StreamResult result = new StreamResult(os);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			t.transform(source, result);
		} catch (IOException ioex) {
			throw new InfraredToolException("Failed to write XML document " + doc + " to file "
					+ file.getAbsolutePath(),ioex);
		} catch (TransformerException e) {
			throw new InfraredToolException("Failed to write XML document " + doc + " to file "
					+ file.getAbsolutePath(),e);
		} finally {
			closeSilently(os);
		}
	}
	
	

	private void closeSilently(Closeable c) {
		try {
			if(c != null)
				c.close();
		} catch (IOException e) {
			// ignoring the closing exception here
			logger.log(Level.FINE, "Error in closing connection", e);
		}
	}

	/**
	 * Dummy resolver so that underlying parser do not use external dtd references
	 * This is required as tool should work event the system is not connected to internet
	 * 
	 *  For apache xerces parser we could have used
	 *  <pre>
	 *  builder.setFeature(
     *	   "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
  	 *  }
	 *  </pre>
	 *  But better we should not have any dependency on the xerces as such
	 * @author chetanm
	 *
	 */
	private static class DummyResolver implements EntityResolver {
		  public InputSource resolveEntity(String publicId, String systemId) {
		    return new InputSource(new StringReader(""));
		  }
	}
}
