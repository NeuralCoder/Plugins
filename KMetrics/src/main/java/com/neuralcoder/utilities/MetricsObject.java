
package com.neuralcoder.utilities;

import com.google.common.base.Strings;

// TODO: Auto-generated Javadoc
/**
 * The Class K1Object.
 */
public class MetricsObject {

	/** The file name. */
	private String fileName;

	/** The coverage type. */
	private String coverageType;

	/** The swatt file path. */
	private String swattFilePath;

	/** The swatt coverage. */
	private Double swattCoverage;

	/**
	 * Instantiates a new k 1 object.
	 *
	 * @param fileName
	 *            the file name
	 * @param coverageType
	 *            the coverage type
	 */
	public MetricsObject(String fileName, String coverageType) {

		if ((!Strings.isNullOrEmpty(fileName)) && (!" ".equals(fileName))) {
			this.fileName = fileName;
		} else {
			this.fileName = "null";
		}

		if ((!Strings.isNullOrEmpty(coverageType)) && (!" ".equals(coverageType))) {
			this.coverageType = coverageType;
		} else {
			this.coverageType = "null";
		}
	}

	/**
	 * Instantiates a new k 1 object.
	 */
	public MetricsObject() {
		super();
	}

	/**
	 * Sets the file name.
	 *
	 * @param name
	 *            the new file name
	 */
	public void setFileName(String name) {
		if ((!Strings.isNullOrEmpty(name)) && (!" ".equals(name))) {
			this.fileName = name;
		} else {
			this.fileName = "null";
		}

	}

	/**
	 * Sets the coverage type.
	 *
	 * @param coverage
	 *            the new coverage type
	 */
	public void setcoverageType(String coverage) {
		if ((!Strings.isNullOrEmpty(coverage)) && (!" ".equals(coverage))) {
			this.coverageType = coverage;
		} else {
			this.coverageType = "null";
		}

	}

	/**
	 * Sets the file path.
	 *
	 * @param path
	 *            the new file path
	 */
	public void setFilePath(String path) {
		if ((!Strings.isNullOrEmpty(path)) && (!" ".equals(path))) {
			this.swattFilePath = path;
		} else {
			this.swattFilePath = "null";
		}

	}

	/**
	 * Sets the swatt coverage.
	 *
	 * @param coverage
	 *            the new swatt coverage
	 */
	public void setSwattCoverage(Double coverage) {
		this.swattCoverage = coverage;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the coverage type.
	 *
	 * @return the coverage type
	 */
	public String getCoverageType() {
		return coverageType;
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return swattFilePath;
	}

	/**
	 * Gets the swatt coverage.
	 *
	 * @return the swatt coverage
	 */
	public Double getSwattCoverage() {
		return swattCoverage;
	}
}