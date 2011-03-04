package com.pulseenergy.oss.hoptoad.log4j;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HoptoadNotifier;
import com.pulseenergy.oss.hoptoad.httpclient.HttpClientHoptoadNotifier;

@RunWith(MockitoJUnitRunner.class)
public class AbstractHoptoadLog4jAppenderTest {
	private static final int EXPECTED_TIMEOUT = 1000;
	private static final String EXPECTED_VERSION = "2.0";
	private static final String EXPECTED_ENVIRONMENT = "UNIT_TEST";
	private static final String EXPECTED_API_KEY = "q09e8r980e";

	private final class StubHoptoadLog4jAppender extends AbstractHoptoadLog4jAppender {
		@Override
		protected HoptoadNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL) {
			return hoptoadNotifier;
		}
	}

	private static final Logger LOGGER = Logger.getLogger(AbstractHoptoadLog4jAppenderTest.class);
	private static final String EXPECTED_MESSAGE = "This is the expected message";
	private static final Throwable SIMPLE_EXCEPTION = new RuntimeException("SIMULATED EXCEPTION");
	private static final String EXPECTED_URI = "http://unit.test.org/test";
	private static final String EXPECTED_NODE_NAME = "Node 1";
	private static final String EXPECTED_COMPONENT_NAME = "hoptoad4j";
	private final AbstractHoptoadLog4jAppender appender = new StubHoptoadLog4jAppender();

	@Mock
	private HttpClientHoptoadNotifier hoptoadNotifier;
	@Captor
	private ArgumentCaptor<Hoptoad4jNotice> notificationCaptor;

	@Test
	public void logSimpleExceptionProgrammatically() throws Exception {
		appender.setApiKey(EXPECTED_API_KEY);
		appender.setEnvironment(EXPECTED_ENVIRONMENT);
		appender.setTimeoutInMillis(EXPECTED_TIMEOUT);
		appender.setHoptoadUri(EXPECTED_URI);
		appender.setNodeName(EXPECTED_NODE_NAME);
		appender.setComponentName(EXPECTED_COMPONENT_NAME);
		appender.activateOptions();
		final LoggingEvent event = new LoggingEvent(getClass().getName(), LOGGER, Level.WARN, EXPECTED_MESSAGE, SIMPLE_EXCEPTION);
		appender.doAppend(event);
		verify(hoptoadNotifier).send(notificationCaptor.capture());
		final Hoptoad4jNotice notification = notificationCaptor.getValue();
		assertThat(notification.getApiKey(), is(EXPECTED_API_KEY));
		assertThat(notification.getVersion(), is(EXPECTED_VERSION));
		assertThat(notification.getEnvironmentName(), is(EXPECTED_ENVIRONMENT));
		assertThat(notification.getThrowable(), is(SIMPLE_EXCEPTION));
		assertThat(notification.getErrorMessage(), is(EXPECTED_MESSAGE));
		assertThat(notification.getNodeName(), is(EXPECTED_NODE_NAME));
		assertThat(notification.getComponentName(), is(EXPECTED_COMPONENT_NAME));
		assertThat(notification.getErrorClass(), is(getClass().getName()));
	}

}
