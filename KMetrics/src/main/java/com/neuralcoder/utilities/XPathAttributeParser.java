package com.neuralcoder.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class XPathAttributeParser.
 */
public class XPathAttributeParser {
	
	/** The metrics name. */
	private String metricsName;
	
	/** The file properties. */
	private String fileProperties;
	

	
	/**
	 * Instantiates a new x path attribute parser.
	 *
	 * @param projectMetricsFile the project metrics file
	 */
	public XPathAttributeParser(String projectMetricsFile) {
		this.metricsName = projectMetricsFile;
	}
	
	/**
	 * Instantiates a new x path attribute parser.
	 *
	 * @param projectMetricsFile the project metrics file
	 * @param fileProperties the file properties
	 */
	public XPathAttributeParser(String projectMetricsFile, String fileProperties) {
		this.metricsName = projectMetricsFile;
		this.fileProperties = fileProperties;
	}
	
	/**
	 * Check xml node attribute.
	 *
	 * @param nList the n list
	 * @param type the type
	 * @return a nodes list attributes if type is "Attribute" or nodes names list
	 */
	public static final ArrayList<String> checkXmlNodeAttribute(NodeList nList, String type) {
		ArrayList<String> attribute = new ArrayList<String>();
		ArrayList<String> name = new ArrayList<String>();
		Validation valid = new Validation();
		
		for (int i = 0; i < nList.getLength(); i++) 
		{
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) node;
				String nodeAttr = eElement.getAttribute("Kind");
				
				if ("C0".equalsIgnoreCase(nodeAttr) || "C1".equalsIgnoreCase(nodeAttr) || "MCDC".equalsIgnoreCase(nodeAttr)) 
				{
					Node parentNode = eElement.getParentNode().getParentNode().getParentNode();
					
					NodeList toCount = parentNode.getChildNodes();
					for (int j = 0; j < toCount.getLength(); j++) 
					{
						Node count = toCount.item(j);
						if (count.getNodeType() != Node.ELEMENT_NODE)
							continue;
						Element dec = (Element) count;
						String childNodes = dec.getNodeName();
						if ("Decomposition".equals(childNodes)) 
						{
							NodeList countFinal = dec.getChildNodes();
							for (int k = 0; k < countFinal.getLength(); k++) 
							{
								Node finalNode = countFinal.item(k);
								if (finalNode.getNodeType() != Node.ELEMENT_NODE)
									continue;
								Element finalElement = (Element) finalNode;
								String finalFiles = finalElement.getAttribute("Name");
								if (valid.findExtension(finalFiles) == true) 
								{
									attribute.add(nodeAttr);
									name.add(valid.removeEmptySpace(finalFiles));
								}
							}
						}
					}
				}
			}
		}
		if ("Attribute".equals(type)) {
			return attribute;
		} else {
			return name;
			
		}
		
	}
	
	/**
	 * Gets the nodes list from xml.
	 *
	 * @param fileName the file name
	 * @param findNodes the find nodes
	 * @return the nodes list from xml
	 */
	public static final NodeList getNodesListFromXml(String fileName, String findNodes) {
		
		NodeList nList = null;
		FileInputStream fXmlFile = null;
		
		try {
			fXmlFile = new FileInputStream(new File(fileName));
			
		} 
		
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
			
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fXmlFile);
			document.getDocumentElement().normalize();
			nList = document.getElementsByTagName(findNodes);
			
		} catch (ParserConfigurationException | SAXException | IOException | IllegalArgumentException e) {
			e.printStackTrace();
			
		}finally{
			try{
				if (fXmlFile != null)
					fXmlFile.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		return nList;
	}
}

