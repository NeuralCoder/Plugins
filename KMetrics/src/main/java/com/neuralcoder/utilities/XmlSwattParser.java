
package com.neuralcoder.utilities;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class XmlSwattParser.
 */

public final class XmlSwattParser extends XPathAttributeParser {

	/** The file name. */
	String fileName;

	/** The file properties. */
	String fileProperties;

	/** The covered uncovered. */
	private ArrayList<Double> coveredUncovered = new ArrayList<Double>();

	/**
	 * Instantiates a new xml swatt parser.
	 *
	 * @param projectMetricsFile
	 *            the project metrics file
	 * @param fileName
	 *            the file name
	 */
	public XmlSwattParser(String projectMetricsFile, String fileName) {
		super(projectMetricsFile);
		this.fileProperties = projectMetricsFile;
		this.fileName = fileName;

	}

	/**
	 * Instantiates a new xml swatt parser.
	 *
	 * @param projectMetricsFile
	 *            the project metrics file
	 * @param fileProperties
	 *            the file properties
	 * @param fileName
	 *            the file name
	 */
	public XmlSwattParser(String projectMetricsFile, String fileProperties, String fileName) {
		super(projectMetricsFile, fileProperties);
		this.fileName = fileName;
		this.fileProperties = fileProperties;
	}

	/** The file nodes swatt. */
	NodeList fileNodesSwatt = null;

	/**
	 * The nodes attributes.
	 *
	 * @param name
	 *            the name
	 * @param fileName
	 *            the file name
	 * @return the nodes values list
	 */

	/**
	 * Parses the x path attribute.
	 *
	 * @param name
	 *            the name of the nodes that need to be retrieved from
	 *            SW_DASHBOARD.XML
	 * @param fileName
	 *            the fileName representing the name of the SW_DASHBOARD.XML
	 *            file with its complete path
	 * @return a list with the names of the files with one of the coverage
	 *         C0,C1,MCDC. SW_DASWBOARD.XML is the input file
	 */
	public static final ArrayList<String> getNodesValuesList(String name, String fileName) {
		ArrayList<String> codeCoverageList = new ArrayList<String>();
		NodeList nList = getNodesListFromXml(fileName, "CodeCoverage");
		if (null != nList) {
			codeCoverageList = checkXmlNodeAttribute(nList, name);
		} else {
			MetricsLogs.consoleLogs(
					"Can not read " + fileName.substring(fileName.lastIndexOf("\\"), fileName.length()) + "file.");
		}

		return codeCoverageList;
	}

	/**
	 * Gets the file properties nodes values.
	 *
	 * @param fileName
	 *            the file name
	 * @param nodeName
	 *            the node name
	 * @return a list with the specified nodes values.
	 */
	public static final ArrayList<String> getFilePropertiesNodesValues(String fileName, String nodeName) {
		ArrayList<String> filePropertiesList = new ArrayList<>();
		NodeList fpFilesNodeList = XPathAttributeParser.getNodesListFromXml(fileName, nodeName);

		if (fpFilesNodeList != null) {
			for (int i = 0; i < fpFilesNodeList.getLength(); i++) {
				Node node = fpFilesNodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String nodeValue = node.getTextContent();
					filePropertiesList.add(nodeValue);
				}
			}
		}
		return filePropertiesList;
	}

	/**
	 * Add the file coverage and path of each file retrieved from
	 * SW_DASHBOARD.XML (K1) or from files_properties.xml (K1 Lite)
	 *
	 * @param nodesNames
	 *            the nodes names retrieved either from SW_DASHBOARD.XML (K1),
	 *            either from files_properties.xml (K1 Lite)
	 * @param fileNodesSwatt
	 *            the file nodes retrieved from junit_log.xml (SWATT report)
	 * @param identifier
	 *            the identifier representing if this method is called either
	 *            for K1, either for K1 Lite computation
	 * @return the array list, representing the populated K1 objects (fileName,
	 *         coverageType, swattFilePath, coverageValue)
	 */
	private ArrayList<MetricsObject> addSwattPathAndCoverage(ArrayList<MetricsObject> nodesNames, NodeList fileNodesSwatt,
			boolean identifier) {

		// create a clone for the nodesNames to work on them and keep the
		// original collection
		// ArrayList<K1Object> nodesCopy = new ArrayList<>(nodesNames);

		Outer: for (MetricsObject objK1 : nodesNames) {
			for (int i = 0; i < fileNodesSwatt.getLength(); i++) {
				Node node = fileNodesSwatt.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;

					String fullPath = eElement.getAttribute("name");

					String nodeAttr = null;

					try {
						nodeAttr = Paths.get(eElement.getAttribute("name")).getFileName().toString();
					} catch (InvalidPathException e) {
						e.printStackTrace();
					}

					if ((nodeAttr != null) && (nodeAttr.equals(objK1.getFileName()))) {

						// add the SWATT file path
						objK1.setFilePath(fullPath);

						// add the SWATT file coverage
						objK1.setSwattCoverage(getCoverageFormula(node, objK1.getCoverageType(), identifier));

						// go to next file collected from
						// SW_DASHBOARD.XML/files_properties.xml and make the
						// further checks on it, like its predecessor
						continue Outer;
					}

				}
			}
		}

		// return the populated K1 objects
		return nodesNames;
	}

	/**
	 * Gets the nodes values from SWATT (junit_log.xml) file.
	 *
	 * @param fileName
	 *            the file name from which the values are extracted,that
	 *            represents inputs for computation
	 * @param nodesToCollect
	 *            the type of nodes to collect from the file
	 * @param nodesNames
	 *            the files that are already extracted from SW_DASHBOARD.XML
	 *            (K1) or from files_properties.xml (K1lite)
	 * @param identifier
	 *            the identifier that represents if its for K1 or K1lite
	 * @return the nodes values from SWATT file
	 */
	public ArrayList<MetricsObject> getNodesValuesFromSwattFile(String fileName, String nodesToCollect,
			ArrayList<MetricsObject> nodesNames, Boolean identifier) {

		// fileNodesSwatt list returns the files names along with their path
		// existing in SWATT report (junit_log.xml)
		NodeList fileNodesSwatt = getNodesListFromXml(fileName, nodesToCollect);

		// populate the K1 objects with SWATT paths and SWATT covergaes for K1
		// and K1lite
		ArrayList<MetricsObject> filesK1Objects = addSwattPathAndCoverage(nodesNames, fileNodesSwatt, identifier);

		// return the populated SWATT coverages
		return filesK1Objects;
	}

	/**
	 * Verify if the files names from junit_log.xml exists in SW_DASHBOARD(for
	 * K1) or in files_properties.xml (for K1lite)
	 * 
	 * @param nodeNames
	 *            a list containing nodeNames from SW_DASHBOARD(for K1) or
	 *            files_properties(for K1lite)
	 * @param name
	 *            a String representing the name in swatt log
	 * @return TRUE if <b>name</b> is in the list nodesNames, otherwise returns
	 *         FALSE
	 */
	public boolean checkNames(ArrayList<MetricsObject> nodeNames, String name) {

		boolean match = false;
		String swattFile = null;

		try {
			swattFile = Paths.get(name).getFileName().toString();
		} catch (InvalidPathException e) {
			e.printStackTrace();
		}

		for (MetricsObject obj : nodeNames) {
			if ((swattFile != null) && (swattFile.equals(obj.getFileName()))) {
				match = true;
				/**
				 * The file names are unique. Stop to search if it's found.
				 */
				break;
			}
		}
		return match;
	}

	/**
	 * Gets result of the coverage formula.
	 *
	 * @param node
	 *            represents the file name
	 * @param type
	 *            represents the coverage type from SW Dashboard
	 * @param identifier
	 *            the identifier
	 * @return a double value, representing the coverageFormula result
	 */

	public double getCoverageFormula(Node node, String type, boolean identifier) {
		int x = 0;
		double coverageFormula = 0.0;
		String typeCoverage = "";

		// identifier is 'true' when K1 is computed and it applies for
		// C0,C1,MCDC
		if (identifier) {
			if (("C0").equalsIgnoreCase(type)) {
				x = 0;
				typeCoverage = "statement";
			}
			if (("C1").equalsIgnoreCase(type)) {
				x = 1;
				typeCoverage = "decision";
			}
			if (("MCDC").equalsIgnoreCase(type)) {
				typeCoverage = "mcdc";
				x = 2;
			}
		}

		// identifier is 'false' when K1lite is computed and it applies only for
		// MCDC
		else {
			typeCoverage = "mcdc";
			x = 2;
		}

		Node firstNodeChild = getChildsNode(node).get(x);

		if (firstNodeChild.getNodeType() == Node.ELEMENT_NODE) {
			Element fistNodeChildElement = (Element) firstNodeChild;
			if (typeCoverage.equals(fistNodeChildElement.getAttribute("name"))) {

				String covered = fistNodeChildElement.getAttribute("covered");
				String uncovered = fistNodeChildElement.getAttribute("uncovered");
				String uncoveredJustified = fistNodeChildElement.getAttribute("uncovered_justified");

				double sumCoveredUncoveredJustified = Double.parseDouble(covered) + Double.parseDouble(uncovered)
						+ Double.parseDouble(uncoveredJustified);

				double sumCoveredUncovered = Double.parseDouble(covered) + Double.parseDouble(uncoveredJustified);

				coverageFormula = Math.floor(((sumCoveredUncovered / sumCoveredUncoveredJustified) * 100) * 100D) / 100;
			}
		}

		return Double.isNaN(coverageFormula) ? 0.0 : coverageFormula;
	}

	/**
	 * Gets the childs node.
	 *
	 * @param node
	 *            the node
	 * @return the childs node
	 */
	public ArrayList<Node> getChildsNode(Node node) {
		ArrayList<Node> childNodes = new ArrayList<Node>();
		NodeList nodeChilds = node.getChildNodes();
		if (nodeChilds != null) {
			for (int i = 0; i < nodeChilds.getLength(); i++) {
				if (nodeChilds.item(i).getNodeType() == Node.ELEMENT_NODE) {
					childNodes.add(nodeChilds.item(i));
				}
			}
		}
		return childNodes;
	}

	/**
	 * Gets the files number.
	 *
	 * @param nodesNames
	 *            the nodes names
	 * @return the files number
	 */
	public int getFilesNumber(ArrayList<String> nodesNames) {
		return nodesNames.size();
	}

	/**
	 * Gets the covered uncovered.
	 *
	 * @return the covered uncovered
	 */
	public ArrayList<Double> getCoveredUncovered() {
		return coveredUncovered;
	}
}
