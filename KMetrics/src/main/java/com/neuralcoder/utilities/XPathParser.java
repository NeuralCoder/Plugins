package com.neuralcoder.utilities;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XPathParser {

	public Double parseWithXPath(File projectMetricsFile, String query) {

		String value = "";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(projectMetricsFile);

			XPathFactory xpathFactory = XPathFactory.newInstance();

			XPath xpath = xpathFactory.newXPath();

			value = getValueFromXml(document, xpath, query);

		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException | NumberFormatException e) {

		}

		return (("N/A".equals(value)) || (" ".equals(value))) ? 0.0d : Double.parseDouble(value);

	}

	private String getValueFromXml(Document document, XPath xpath, String query) throws XPathExpressionException {

		XPathExpression expression = xpath.compile(query);
		Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
		if (node != null) {
			return node.getNodeValue();
		} else {
			return "N/A";
		}

	}
}
