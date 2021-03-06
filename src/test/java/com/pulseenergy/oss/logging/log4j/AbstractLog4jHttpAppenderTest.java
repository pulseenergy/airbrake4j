package com.pulseenergy.oss.logging.log4j;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pulseenergy.oss.logging.NotificationSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationBuilder;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;

@RunWith(MockitoJUnitRunner.class)
public class AbstractLog4jHttpAppenderTest {
	private static final String EXPECTED_CONTENT_TYPE = "text/plain";
	@Mock
	private HttpNotificationBuilder<String, LoggingEvent> notificationGenerator;
	@Mock
	private NotificationSerializer<String, String> serializer;
	@Mock
	private HttpNotificationSender notificationSender;
	@Captor
	private ArgumentCaptor<String> notificationCaptor;

	private final class StubLog4jAppender extends AbstractLog4jHttpAppender<String> {
		@Override
		protected NotificationSerializer<String, String> buildNotificationSerializer() {
			return serializer;
		}

		@Override
		protected HttpNotificationBuilder<String, LoggingEvent> buildNotificationGenerator() {
			return notificationGenerator;
		}

		@Override
		protected HttpNotificationSender buildNotificationSender() {
			return notificationSender;
		}
	}

	private static final Logger LOGGER = Logger.getLogger(AbstractLog4jHttpAppenderTest.class);
	private static final String EXPECTED_MESSAGE = "This is the expected message";
	private static final Throwable SIMPLE_EXCEPTION = new RuntimeException("SIMULATED EXCEPTION");
	private final AbstractLog4jHttpAppender httpAppender = new StubLog4jAppender();


	@Test
	public void logSimpleExceptionProgrammatically() throws Exception {
		httpAppender.activateOptions();
		final LoggingEvent event = new LoggingEvent(getClass().getName(), LOGGER, Level.WARN, EXPECTED_MESSAGE, SIMPLE_EXCEPTION);
		when(notificationGenerator.build(event)).thenReturn(EXPECTED_MESSAGE);
		when(serializer.serialize(EXPECTED_MESSAGE)).thenReturn(EXPECTED_MESSAGE);
		when(serializer.getContentType()).thenReturn(EXPECTED_CONTENT_TYPE);
		httpAppender.doAppend(event);
		verify(notificationSender).send(notificationCaptor.capture(), eq(EXPECTED_CONTENT_TYPE));
		final String notification = notificationCaptor.getValue();
		assertThat(notification, is(EXPECTED_MESSAGE));
	}

}
