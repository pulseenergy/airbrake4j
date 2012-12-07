package com.pulseenergy.oss.logging.logback;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.After;
import org.junit.Before;
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
public class AbstractLogbackHttpAppenderBaseTest {

	private static final String MESSAGE = "MESSAGE";
	private static final String EXPECTED_CONTENT_TYPE = "text/plain";
	private StubLogbackAppender appender;
	@Mock
	private HttpNotificationBuilder<String, ILoggingEvent> notificationGenerator;
	@Mock
	private HttpNotificationSender notificationSender;
	@Mock
	private NotificationSerializer<String, String> serializer;

	private class StubLogbackAppender extends AbstractLogbackHttpAppenderBase<String> {
		@Override
		protected NotificationSerializer<String, String> buildNotificationSerializer() {
			return serializer;
		}

		@Override
		protected HttpNotificationBuilder<String, ILoggingEvent> buildNotificationGenerator() {
			return notificationGenerator;
		}

		@Override
		protected HttpNotificationSender buildNotificationSender() {
			return notificationSender;
		}
	}

	@Mock
	private ILoggingEvent eventObject;
	@Captor
	private ArgumentCaptor<String> notificationCaptor;

	@Before
	public void setUp() throws Exception {
		when(serializer.getContentType()).thenReturn(EXPECTED_CONTENT_TYPE);
		appender = new StubLogbackAppender();
		appender.start();
	}

	@Test
	public void testAppend() throws IOException {
		when(notificationGenerator.build(eventObject)).thenReturn(MESSAGE);
		when(serializer.serialize(MESSAGE)).thenReturn(MESSAGE);
		appender.append(eventObject);
		verify(notificationSender).send(notificationCaptor.capture(), eq(EXPECTED_CONTENT_TYPE));
		String notification = notificationCaptor.getValue();
		assertThat(notification, is(MESSAGE));
	}

	@After
	public void tearDown () {
		appender.stop();
	}
}
