
package com.neuralcoder.utilities;

import com.google.common.base.Strings;

// TODO: Auto-generated Javadoc
/**
 * The Class DashboardFile.
 */
public class DashboardFile {

	/** The file name. */
	private String fileName;

	/** The is in swatt. */
	private boolean isInSwatt;

	/**
	 * Instantiates a new dashboard file.
	 *
	 * @param fileName
	 *            the file name
	 * @param isInSwatt
	 *            the is in swatt
	 */
	public DashboardFile(String fileName, boolean isInSwatt) {

		if ((!Strings.isNullOrEmpty(fileName)) && (!" ".equals(fileName))) {
			this.fileName = fileName;
		} else {
			this.fileName = "null";
		}

		Object obj = isInSwatt;
		if ((obj.getClass().getName().equals(boolean.class))) {
			this.isInSwatt = isInSwatt;
		} else {
			throw new IllegalArgumentException("It must be a boolean value");
		}

	}

	/**
	 * Instantiates a new dashboard file.
	 */
	public DashboardFile() {
		super();
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		if ((!Strings.isNullOrEmpty(name)) && (!" ".equals(name))) {
			this.fileName = name;
		} else {
			this.fileName = "null";
		}

	}

	/**
	 * Sets the found.
	 *
	 * @param isInSwatt
	 *            the new found
	 */
	public void setFound(boolean isInSwatt) {
		Object obj = isInSwatt;
		if ((obj.getClass().getName().equals(boolean.class))) {
			this.isInSwatt = isInSwatt;
		} else {
			throw new IllegalArgumentException("It must be a boolean value");
		}

	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return fileName;
	}

	/**
	 * Gets the checks if is found.
	 *
	 * @return the checks if is found
	 */
	public boolean getIsFound() {
		return isInSwatt;
	}
}