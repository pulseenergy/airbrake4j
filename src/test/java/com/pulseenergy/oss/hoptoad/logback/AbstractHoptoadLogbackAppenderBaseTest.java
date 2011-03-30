package com.pulseenergy.oss.hoptoad.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEventVO;
import org.junit.Before;
import org.junit.Test;

import com.pulseenergy.oss.hoptoad.HoptoadNotifier;

public class AbstractHoptoadLogbackAppenderBaseTest {

	private StubHoptoadLogbackAppender hoptoadAppender;

	private class StubHoptoadLogbackAppender extends AbstractHoptoadLogbackAppenderBase {

		@Override
		protected HoptoadNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL) {
			return hoptoadNotifier;
		}
	}
	private HoptoadNotifier hoptoadNotifier;

	@Before
	public void setUp() throws Exception {
		hoptoadAppender = new StubHoptoadLogbackAppender();
		hoptoadAppender.start();
	}

	@Test
	public void testAppend() {
		final ILoggingEvent eventObject = new LoggingEventVO();

		hoptoadAppender.append(eventObject);

	}
}
