package com.pulseenergy.oss.airbrake.log4j;

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

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.airbrake.AirbrakeNotifier;
import com.pulseenergy.oss.airbrake.httpclient.HttpClientAirbrakeNotifier;

@RunWith(MockitoJUnitRunner.class)
public class AbstractAirbrakeLog4jAppenderTest {
	private static final int EXPECTED_TIMEOUT = 1000;
	private static final String EXPECTED_VERSION = "2.0";
	private static final String EXPECTED_ENVIRONMENT = "UNIT_TEST";
	private static final String EXPECTED_API_KEY = "q09e8r980e";

	private final class StubAirbrakeLog4jAppender extends AbstractAirbrakeLog4jAppender {
		@Override
		protected AirbrakeNotifier buildAirbrakeNotifier(final int timeoutInMillis, final String airbrakeUri, final boolean useSSL) {
			return airbrakeNotifier;
		}
	}

	private static final Logger LOGGER = Logger.getLogger(AbstractAirbrakeLog4jAppenderTest.class);
	private static final String EXPECTED_MESSAGE = "This is the expected message";
	private static final Throwable SIMPLE_EXCEPTION = new RuntimeException("SIMULATED EXCEPTION");
	private static final String EXPECTED_URI = "http://unit.test.org/test";
	private static final String EXPECTED_NODE_NAME = "Node 1";
	private static final String EXPECTED_COMPONENT_NAME = "airbrake4j";
	private final AbstractAirbrakeLog4jAppender appender = new StubAirbrakeLog4jAppender();

	@Mock
	private HttpClientAirbrakeNotifier airbrakeNotifier;
	@Captor
	private ArgumentCaptor<Airbrake4jNotice> notificationCaptor;

	@Test
	public void logSimpleExceptionProgrammatically() throws Exception {
		appender.setApiKey(EXPECTED_API_KEY);
		appender.setEnvironment(EXPECTED_ENVIRONMENT);
		appender.setTimeoutInMillis(EXPECTED_TIMEOUT);
		appender.setAirbrakeUri(EXPECTED_URI);
		appender.setNodeName(EXPECTED_NODE_NAME);
		appender.setComponentName(EXPECTED_COMPONENT_NAME);
		appender.activateOptions();
		final LoggingEvent event = new LoggingEvent(getClass().getName(), LOGGER, Level.WARN, EXPECTED_MESSAGE, SIMPLE_EXCEPTION);
		appender.doAppend(event);
		verify(airbrakeNotifier).send(notificationCaptor.capture());
		final Airbrake4jNotice notification = notificationCaptor.getValue();
		assertThat(notification.getApiKey(), is(EXPECTED_API_KEY));
		assertThat(notification.getVersion(), is(EXPECTED_VERSION));
		assertThat(notification.getEnvironmentName(), is(EXPECTED_ENVIRONMENT));
		assertThat(notification.getThrowableData().getClassName(), is(SIMPLE_EXCEPTION.getClass().getName()));
		assertThat(notification.getErrorMessage(), is(EXPECTED_MESSAGE));
		assertThat(notification.getNodeName(), is(EXPECTED_NODE_NAME));
		assertThat(notification.getComponentName(), is(EXPECTED_COMPONENT_NAME));
		assertThat(notification.getErrorClass(), is(getClass().getName()));
	}

}
