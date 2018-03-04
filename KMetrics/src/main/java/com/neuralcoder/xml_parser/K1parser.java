package com.neuralcoder.xml_parser;

import static com.neuralcoder.metrics.EMetricsModelType.K1;
import static com.neuralcoder.metrics.EMetricsModelType.K1Lite;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.neuralcoder.metrics.MetricsModel;
import com.neuralcoder.utilities.GlobalProperties;
import com.neuralcoder.utilities.MetricsObject;
import com.neuralcoder.utilities.MetricsLogs;
import com.neuralcoder.utilities.MetricsUtilities;
import com.neuralcoder.utilities.PropertiesReader;
import com.neuralcoder.utilities.ResultsSaver;
import com.neuralcoder.utilities.Validation;
import com.neuralcoder.utilities.XmlSwattParser;

import hudson.model.AbstractBuild;

// TODO: Auto-generated Javadoc
/**
 * The Class K1parser. This class has its main responsibility to retrieve from
 * SW_DASHBOARD.XML or files_properties.xml (K1lite) and on the other side,
 * from the junit_log.xml (SWATT report) the relevant files. AFter this is done
 * the K1 or/and K1lite computation is performed based on data extracted from
 * above presented files.
 */

public class K1parser extends AbstractParser {

	/** The logger. */
	public PrintStream logger;

	/**
	 * Instantiates a new k1parser.
	 *
	 * @param projectMetricsFile
	 *            the project metrics file
	 * @param propertiesReader
	 *            the properties reader
	 * @param buildNumber
	 *            the build number
	 * @param metricsModel
	 *            the metrics model
	 * @param build
	 *            the build
	 */
	public K1parser(ArrayList<File> projectMetricsFile, PropertiesReader propertiesReader, int buildNumber,
			MetricsModel metricsModel, AbstractBuild<?, ?> build) {
		super(projectMetricsFile, propertiesReader, buildNumber, metricsModel, build);
		this.projectMetricsFile = projectMetricsFile;
		this.abstractbuild = build;
	}

	/** The saver. */
	ResultsSaver saver = new ResultsSaver(abstractbuild);

	/** The destination path. */
	File destinationPath = new File(abstractbuild.getRootDir(), GlobalProperties.K_FOLDER);

	/** The kpi path. */
	File kpiPath = MetricsUtilities.getKPIFolder(abstractbuild);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.neuralcoder.xml_parser.AbstractParser#extractValuesFromXML()
	 */

	@Override
	protected void computeKMetrics() {
		extractK1LiteFromXML();
		if (GlobalProperties.SW_DASHBOARD_FILE.length() > 0) {
			extractK1FromXML();
		}
		saver.deleteInputFiles(logger);
	}

	/**
	 * create on the master machine K1.txt file, containing K1 value for each build
	 * add k1 build value to model mu.copyFile functions copies K1/K1lite from buil
	 * number to KPI folder at build level
	 */
	private void extractK1FromXML() {
		double K1Value = computeK1();
		saver.createFile(destinationPath, "K1.txt", Double.toString(K1Value));
		MetricsUtilities.copyFile(destinationPath.toString(), kpiPath.toString(), "K1.txt");
		metricsModel.addMetrics(K1, buildNumber, K1Value);
	}

	/**
	 * create on the master machine K1Lite.txt file, containing K1Lite value for
	 * each build add k1lite build value to model
	 */
	private void extractK1LiteFromXML() {
		double K1LiteFormula = computeK1lite();
		saver.createFile(destinationPath, "K1lite.txt", Double.toString(K1LiteFormula));
		MetricsUtilities.createKPIFolder(abstractbuild);
		MetricsUtilities.copyFile(destinationPath.toString(), kpiPath.toString(), "K1lite.txt");
		metricsModel.addMetrics(K1Lite, buildNumber, K1LiteFormula);
	}

	/**
	 * Gets the K1 log.The method is called either for K1, either for K1 Lite Its
	 * purpose is to add the objects collected from the ArrayList to a String
	 *
	 * @param K1Objects
	 *            the k 1 objects
	 * @return the k 1 lite log
	 */
	private String getK1Log(ArrayList<MetricsObject> K1Objects) {
		StringBuilder k1Log = new StringBuilder();

		Iterator<MetricsObject> it = K1Objects.iterator();

		while (it.hasNext()) {
			MetricsObject obj = it.next();
			k1Log.append(obj.getFileName()).append("|").append(obj.getCoverageType()).append("|")
					.append(obj.getFilePath()).append("|");

			if (it.hasNext())
				k1Log.append(obj.getSwattCoverage()).append("|");
			else
				k1Log.append(obj.getSwattCoverage());
		}

		return k1Log.toString();

	}

	/**
	 * Compute K1lite.
	 *
	 * @return the double result of K1lite
	 */
	private double computeK1lite() {
		double k1LiteValue = 0.0;

		// Retrieve from Jenkins job configuration the field containing
		// junit_log.xml file
		String junitLogXML = projectMetricsFile.get(1).toString();

		// Retrieve from Jenkins job configuration the field containing
		// files_properties.xml file
		String filesPropertiesXML = projectMetricsFile.get(2).toString();

		// Extract from files_properties.xml file the <FileName> and
		// <SourceCodeCategory> nodes
		ArrayList<String> fpFileNameNodes = XmlSwattParser.getFilePropertiesNodesValues(filesPropertiesXML, "FileName");
		ArrayList<String> fpFilesSourceCodeCategNodes = XmlSwattParser.getFilePropertiesNodesValues(filesPropertiesXML,
				"SourceCodeCategory");

		// Store in the container only the *.c files with 'New Code' and
		// 'Modified Reused' categories
		ArrayList<String> fpFilesDotCNewCodeModifiedReused = getNewCodeModifiedNamesFP(fpFileNameNodes,
				fpFilesSourceCodeCategNodes);

		ArrayList<MetricsObject> K1liteObjects = getK1ObjectFormat(fpFilesDotCNewCodeModifiedReused, null, false);

		// Retrieve from SWATT file( junit_log.xml ) the nodes <file>
		XmlSwattParser xmlSwattParserFP = new XmlSwattParser(filesPropertiesXML, junitLogXML);
		ArrayList<MetricsObject> fileK1liteObjectsList = xmlSwattParserFP.getNodesValuesFromSwattFile(junitLogXML, "file",
				K1liteObjects, false);

		/*
		 * Sort the objects alphabetically after the fileName field (Name from
		 * files_properties.xml) This lambda expression is used for sorting
		 * alphabetically the K1 Objects list on the base of their corresponding
		 * fileName
		 */
		Collections.sort(fileK1liteObjectsList, (MetricsObject obj1, MetricsObject obj2) -> {
			return obj1.getFileName().compareToIgnoreCase(obj2.getFileName());
		});

		// Retrieve the files having New Code and Modified Reused category
		// present in files_properties.xml,in SWATT and also files not present
		// in SWATT
		String k1LiteLog = getK1Log(fileK1liteObjectsList);

		// Create the K1LiteLogFile.txt
		saver.createFile(destinationPath, "K1LiteLogFile.txt", k1LiteLog);

		// Retrieve the coverage values in order to create the K1lite average
		ArrayList<Double> fileCoverageValuesListWithoutSWD = getCoverageValues(fileK1liteObjectsList);

		// Compute the K1Lite average on all files present in SWATT
		k1LiteValue = sum(fileCoverageValuesListWithoutSWD, fpFilesDotCNewCodeModifiedReused.size());

		// Rounding the result, obtaining 2 decimals of the resulting value
		double roundedK1LiteValue = Math.round(k1LiteValue * 100.0) / 100.0;

		// Display the result in the job console
		MetricsLogs.consoleLogs("\nThe K1 lite value is: " + roundedK1LiteValue);

		return roundedK1LiteValue;
	}

	/**
	 * Compute K1.
	 *
	 * @return the double result of K1
	 */
	private double computeK1() {
		double k1Value = 0.0;

		// Retrieve from Jenkins job configuration the field containing
		// SW_DASHBOARD.XML file location
		String fileSWDashboardXML = projectMetricsFile.get(0).toString();

		// Retrieve from Jenkins job configuration the field containing
		// junit_log.xml file
		String junitLogXML = projectMetricsFile.get(1).toString();

		ArrayList<String> fileNamesListSWD = new ArrayList<>();
		ArrayList<String> coverageTypeListSWD = new ArrayList<>();

		// fileNamesListSWD contains the files names from SW_DASHBOARD.XML
		fileNamesListSWD = XmlSwattParser.getNodesValuesList("Name", fileSWDashboardXML);

		// coverageTypeListSWD contains the coverages afferents to the files in
		// fileNamesListSWD
		coverageTypeListSWD = XmlSwattParser.getNodesValuesList("Attribute", fileSWDashboardXML);

		// Store the fileNamesListSWD and coverageTypeListSWD in a list of
		// MetricsObjects
		ArrayList<MetricsObject> K1Objects = getK1ObjectFormat(fileNamesListSWD, coverageTypeListSWD, true);

		// List contains the sum of covered and uncovered attributes foreach .c
		// file collected from SW_DASHBOARD and found in junit_log.xml
		XmlSwattParser xmlSwattParser = new XmlSwattParser(fileSWDashboardXML, junitLogXML);
		ArrayList<MetricsObject> fileK1PopulatedObjects = xmlSwattParser.getNodesValuesFromSwattFile(junitLogXML, "file",
				K1Objects, true);

		/*
		 * Sort the objects alphabetically after the fileName field (Name from
		 * files_properties.xml) This lambda expression is used for sorting
		 * alphabetically the K1 Objects list on the base of their corresponding
		 * fileName
		 */
		Collections.sort(fileK1PopulatedObjects, (MetricsObject obj1, MetricsObject obj2) -> {
			return obj1.getFileName().compareToIgnoreCase(obj2.getFileName());
		});

		// Retrieve the files present in SW_DASHBOARD, present in SWATT and in
		// SW_DASHBOARD, but also files not present in SWATT but present in
		// SW_DASHBOARD
		String k1Log = getK1Log(fileK1PopulatedObjects);

		// Create the K1LiteLogFile.txt
		saver.createFile(destinationPath, "K1LogFile.txt", k1Log);

		// Retrieve the coverage values in order to create the K1 average
		ArrayList<Double> coverageValuesList = getCoverageValues(fileK1PopulatedObjects);

		// Compute the K1 average on all files present in SWATT
		k1Value = sum(coverageValuesList, coverageTypeListSWD.size());

		// Rounding the result, obtaining 2 decimals of the resulting value
		double roundedK1Value = Math.round(k1Value * 100.0) / 100.0;

		// Display the result in the job console
		MetricsLogs.consoleLogs("\nThe K1 value is: " + roundedK1Value);

		return roundedK1Value;

	}

	/**
	 * Gets the k 1 object format.
	 *
	 * @param fileNames
	 *            the file names
	 * @param coverageTypes
	 *            the coverage types of each file (C0, C1, MCDC)
	 * @param identifier
	 *            the identifier representing if this method is called for K1
	 *            computation (true) or for K1lite (false)
	 * @return the k 1 object populated with format for files and their covergae
	 *         types
	 */
	public ArrayList<MetricsObject> getK1ObjectFormat(ArrayList<String> fileNames, ArrayList<String> coverageTypes,
			boolean identifier) {

		ArrayList<MetricsObject> kObjects = new ArrayList<>();

		// in case the method is called for K1 computation
		if (identifier) {
			for (int i = 0; i < fileNames.size(); i++) {
				kObjects.add(new MetricsObject(fileNames.get(i), coverageTypes.get(i)));
			}
		}

		// in case the method is called for K1lite computation, the coverage
		// type is by default MCDC
		else {
			for (int i = 0; i < fileNames.size(); i++) {
				kObjects.add(new MetricsObject(fileNames.get(i), "MCDC"));
			}
		}

		// return the files + coverage types stored in objects
		return kObjects;
	}

	/**
	 * Gets the coverage values.
	 *
	 * @param K1Objects
	 *            the k 1 objects
	 * @return the coverage values
	 */
	public ArrayList<Double> getCoverageValues(ArrayList<MetricsObject> K1Objects) {
		ArrayList<Double> values = new ArrayList<>();

		for (int i = 0; i < K1Objects.size(); i++) {

			if (K1Objects.get(i).getSwattCoverage() != null) {
				values.add(K1Objects.get(i).getSwattCoverage());
			}
		}

		return values;
	}

	/**
	 * Sum.
	 *
	 * @param coveragesValues
	 *            the coverages values
	 * @param coveragePowerPerFile
	 *            the coverage power per file
	 * @return the double
	 */
	public double sum(ArrayList<Double> coveragesValues, int coveragePowerPerFile) {
		double sum = 0.0;
		double formula = 0.0;
		sum = calculateSum(coveragesValues);
		if (coveragePowerPerFile == 0) {
			return formula;
		} else {
			formula = sum / coveragePowerPerFile;
		}
		return formula;
	}

	/**
	 * Gets the New Code and Modified reused from file_properties.xml
	 *
	 * @param filesPropertiesNames
	 *            the files properties names
	 * @param filesPropertiesCategory
	 *            the files properties category
	 * @return a list,having '.c' extension in <FileName> and 'Modified Reused'/'New
	 *         Code' in <SourceCodeCategory>
	 */

	public ArrayList<String> getNewCodeModifiedNamesFP(ArrayList<String> filesPropertiesNames,
			ArrayList<String> filesPropertiesCategory) {
		ArrayList<String> nameNewCodeModifiedFPList = new ArrayList<String>();
		Validation valid = new Validation();

		// Iterate through the <SourceCodeCategory> nodes list and through the
		// <FileName> nodes list
		for (int i = 0; i < filesPropertiesCategory.size(); i++) {
			String fileNameFP = filesPropertiesNames.get(i);
			String fileCategoryFP = filesPropertiesCategory.get(i);

			// If extension is '.c' and its category is New Code/Modified
			// Reused,add the item in the container
			if (valid.findExtension(fileNameFP) && (fileCategoryFP.replaceAll("\\s", "").equalsIgnoreCase("NewCode")
					|| fileCategoryFP.replaceAll("\\s", "").equalsIgnoreCase("ModifiedReused"))) {
				nameNewCodeModifiedFPList.add(fileNameFP);
			}
		}

		return nameNewCodeModifiedFPList;
	}

	/**
	 * Calculate the sum.
	 *
	 * @param sumList
	 *            the sum list
	 * @return the double
	 */
	private double calculateSum(ArrayList<Double> sumList) {
		double sum = 0.0;
		if (sumList.size() > 0) {
			for (int i = 0; i < sumList.size(); i++) {
				sum = (sumList.get(i)) + sum;
			}
		}

		return Math.round(sum * 100.0) / 100.0;
	}
}
//