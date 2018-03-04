
package com.neuralcoder.utilities;

import java.io.PrintStream;
import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class MetricsLogs.
 */
public class MetricsLogs implements Serializable {

	/** The logger. */
	public static PrintStream logger;

	/**
	 * Console logs.
	 *
	 * @param message
	 *            the message
	 */
	public static void consoleLogs(String message) {
		logger.println(message);
	}
}