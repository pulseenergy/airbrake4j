package com.pulseenergy.oss.airbrake4j.it;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Test;

public class Airbrake4jLog4jIT {
	private static final Logger LOGGER = Logger.getLogger(Airbrake4jLog4jIT.class);

	@Test
	public void sendWarningMessage() {
		LOGGER.warn("This is a warning message from " + getClass().getName());
	}

	@Test
	public void sendException() {
		try {
			throwNewException("SIMULATED EXCEPTION");
		} catch (final Exception e) {
			LOGGER.error("Error message with simulated exception", e);
		}
	}

	@Test
	public void setNestedException() {
		try {
			throwNewException("SIMULATED EXCEPTION");
		} catch (final Exception e) {
			try {
				throwNewException("SIMULATED EXCEPTION", e);
			} catch (final Exception another) {
				LOGGER.error("Error message with simulated nested exception", another);
			}
		}
	}

	@AfterClass
	public static void tearDownClass() throws InterruptedException {
		Thread.sleep(10000);
	}

	private void throwNewException(final String exceptionMessage) throws Exception {
		throw new Exception(exceptionMessage);
	}

	private void throwNewException(final String exceptionMessage, final Exception e) throws Exception {
		throw new Exception(exceptionMessage, e);
	}
}
