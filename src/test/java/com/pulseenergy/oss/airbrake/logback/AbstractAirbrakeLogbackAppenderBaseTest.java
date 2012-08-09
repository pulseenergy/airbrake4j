package com.pulseenergy.oss.airbrake.logback;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.helpers.MessageFormatter;

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.airbrake.AirbrakeNotifier;

@RunWith(MockitoJUnitRunner.class)
public class AbstractAirbrakeLogbackAppenderBaseTest {

	private static final Throwable THROWABLE = new RuntimeException("SIMULATED");
	private static final String MESSAGE = "MESSAGE";
	private static final String MESSAGE_WITH_ARGS_PREFIX = "MESSAGE WITH ARGS ";
	private static final String MESSAGE_WITH_ARGS = MESSAGE_WITH_ARGS_PREFIX + "{}";
	private StubAirbrakeLogbackAppender airbrakeAppender;

	private class StubAirbrakeLogbackAppender extends AbstractAirbrakeLogbackAppenderBase {
		@Override
		protected AirbrakeNotifier buildAirbrakeNotifier(final int timeoutInMillis, final String airbrakeUri, final boolean useSSL) {
			return airbrakeNotifier;
		}
	}

	@Mock
	private AirbrakeNotifier airbrakeNotifier;
	@Mock
	private ILoggingEvent eventObject;
	@Mock
	private IThrowableProxy throwableProxy;
	@Mock
	private StackTraceElementProxy stackTraceElementProxy;
	@Captor
	private ArgumentCaptor<Airbrake4jNotice> notificationCaptor;

	@Before
	public void setUp() throws Exception {

		airbrakeAppender = new StubAirbrakeLogbackAppender();
		airbrakeAppender.start();
	}

	@Test
	public void testAppend() throws IOException {
		when(throwableProxy.getClassName()).thenReturn(THROWABLE.getClass().getName());
		when(throwableProxy.getStackTraceElementProxyArray()).thenReturn(Collections.singletonList(stackTraceElementProxy).toArray(new StackTraceElementProxy[1]));
		when(eventObject.getThrowableProxy()).thenReturn(throwableProxy);
		when(eventObject.getFormattedMessage()).thenReturn(MESSAGE);
		airbrakeAppender.append(eventObject);
		verify(airbrakeNotifier).send(notificationCaptor.capture());
		Airbrake4jNotice notification = notificationCaptor.getValue();
		assertThat(notification.getErrorMessage(), is(MESSAGE));
		assertThat(notification.getThrowableData().getClassName(), is(THROWABLE.getClass().getName()));
	}

	@Test
	public void testAppendMessageWithArguments() throws IOException {
		when(throwableProxy.getClassName()).thenReturn(THROWABLE.getClass().getName());
		when(throwableProxy.getStackTraceElementProxyArray()).thenReturn(Collections.singletonList(stackTraceElementProxy).toArray(new StackTraceElementProxy[1]));
		when(eventObject.getThrowableProxy()).thenReturn(throwableProxy);
		final String argument = "argument";
		final Object[] argArray = {argument};
		when(eventObject.getFormattedMessage()).thenReturn(MessageFormatter.arrayFormat(MESSAGE_WITH_ARGS, argArray));
		when(eventObject.getArgumentArray()).thenReturn(argArray);
		airbrakeAppender.append(eventObject);
		verify(airbrakeNotifier).send(notificationCaptor.capture());
		Airbrake4jNotice notification = notificationCaptor.getValue();
		assertThat(notification.getErrorMessage(), is(MESSAGE_WITH_ARGS_PREFIX + argument));
		assertThat(notification.getThrowableData().getClassName(), is(THROWABLE.getClass().getName()));
	}
}
