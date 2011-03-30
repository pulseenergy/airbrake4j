package com.pulseenergy.oss.hoptoad4j.it;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hoptoad4jLogbackIT {
	private static final Logger LOGGER = LoggerFactory.getLogger(Hoptoad4jLogbackIT.class);

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

	private void throwNewException(final String exceptionMessage) throws Exception {
		throw new Exception(exceptionMessage);
	}

	private void throwNewException(final String exceptionMessage, final Exception e) throws Exception {
		throw new Exception(exceptionMessage, e);
	}
}
