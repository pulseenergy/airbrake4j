package com.pulseenergy.oss.logback;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pulseenergy.oss.http.HttpNotificationBuilder;
import com.pulseenergy.oss.http.HttpNotificationSender;

@RunWith(MockitoJUnitRunner.class)
public class AbstractLogbackHttpAppenderBaseTest {

	private static final Throwable THROWABLE = new RuntimeException("SIMULATED");
	private static final String MESSAGE = "MESSAGE";
	private static final String MESSAGE_WITH_ARGS_PREFIX = "MESSAGE WITH ARGS ";
	private static final String MESSAGE_WITH_ARGS = MESSAGE_WITH_ARGS_PREFIX + "{}";
	private StubLogbackAppender appender;
	@Mock
	private HttpNotificationBuilder<String, ILoggingEvent> notificationGenerator;
	@Mock
	private HttpNotificationSender<String> notificationSender;

	private class StubLogbackAppender extends AbstractLogbackHttpAppenderBase<String> {
		@Override
		protected HttpNotificationBuilder<String, ILoggingEvent> buildNotificationGenerator() {
			return notificationGenerator;
		}

		@Override
		protected HttpNotificationSender<String> buildNotificationSender() {
			return notificationSender;
		}
	}

	@Mock
	private ILoggingEvent eventObject;
//	@Mock
//	private IThrowableProxy throwableProxy;
//	@Mock
//	private StackTraceElementProxy stackTraceElementProxy;
	@Captor
	private ArgumentCaptor<String> notificationCaptor;

	@Before
	public void setUp() throws Exception {

		appender = new StubLogbackAppender();
		appender.start();
	}

	@Test
	public void testAppend() throws IOException {
//		when(throwableProxy.getClassName()).thenReturn(THROWABLE.getClass().getName());
//		when(throwableProxy.getStackTraceElementProxyArray()).thenReturn(Collections.singletonList(stackTraceElementProxy).toArray(new StackTraceElementProxy[1]));
//		when(eventObject.getThrowableProxy()).thenReturn(throwableProxy);
//		when(eventObject.getFormattedMessage()).thenReturn(MESSAGE);
		when(notificationGenerator.build(eventObject)).thenReturn(MESSAGE);
		appender.append(eventObject);
		verify(notificationSender).send(notificationCaptor.capture());
		String notification = notificationCaptor.getValue();
		assertThat(notification, is(MESSAGE));
	}
}
