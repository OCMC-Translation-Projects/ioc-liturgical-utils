package org.ocmc.ioc.liturgical.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provides a standardized error reporting mechanism
 * @author mac002
 *
 */
public class ErrorUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ErrorUtils.class);
	
	private static int errorCount = 0;
	private static boolean hadErrors = false;
	private static int overallErrorCount = 0;

	/**
	 * Report an error
	 * @param logger for the reporting class
	 * @param e the exception
	 */
	public static void report(Logger logger, Exception e) {
		logger.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
	}

	/**
	 * Report an error
	 * @param logger for the reporting class
	 * @param e the exception
	 * @param message an additional message to add
	 */
	public static void report(Logger logger, Exception e, String message) {
		incrementErrorCount();
		logger.error(message);
		logger.error(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
	}
	
	/**
	 * Increments the number of errors
	 */
	private static void incrementErrorCount() {
		errorCount++;
		overallErrorCount++;
		hadErrors = true;
	}
	
	/**
	 * Returns the number of errors reported
	 * @return the number of errors reported
	 */
	public static int getErrorCount() {
		return errorCount;
	}
	
	/**
	 * Resets the error count to zero
	 */
	public static void resetErrorCount() {
		errorCount = 0;
	}
	
	/**
	 * Do we have errors reported?
	 * @return true if yes
	 */
	public static boolean hadErrors() {
		return errorCount > 0;
	}
	
	/**
	 * Reports error count and provides supplied message.
	 * Resets error count to zero.
	 * @param message to be added to the error report
	 */
	public static void reportAnyErrors(String message) {
		if (ErrorUtils.hadErrors()) {
			LOGGER.info(message);
			LOGGER.info("Number of errors: " + ErrorUtils.getErrorCount());
			LOGGER.info("Please check the log for details...");
		}
		resetErrorCount();
	}

	/**
	 * Reports error count and provides supplied message.
	 * Resets error count to zero.
	 * @param message to be added to the error report
	 */
	public static void reportTotalErrors(String message) {
		if (ErrorUtils.hadErrors()) {
			LOGGER.info(message);
			LOGGER.info("Number of errors: " + ErrorUtils.getErrorCount());
			LOGGER.info("Please check the log for details...");
		}
	}
}
