package com.pulseenergy.oss.hoptoad.logback;

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
import org.slf4j.LoggerFactory;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HoptoadNotifier;

@RunWith(MockitoJUnitRunner.class)
public class AbstractHoptoadLogbackAppenderBaseTest {

	private static final Throwable THROWABLE = new RuntimeException("SIMULATED");
	private static final String MESSAGE = "MESSAGE";
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractHoptoadLogbackAppenderBaseTest.class);
	private StubHoptoadLogbackAppender hoptoadAppender;
	private class StubHoptoadLogbackAppender extends AbstractHoptoadLogbackAppenderBase {


		@Override
		protected HoptoadNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL) {
			return hoptoadNotifier;
		}

	}
	@Mock
	private HoptoadNotifier hoptoadNotifier;
	@Mock
	private ILoggingEvent eventObject;
	@Mock
	private IThrowableProxy throwableProxy;
	@Mock
	private StackTraceElementProxy stackTraceElementProxy;
	@Captor
	private ArgumentCaptor<Hoptoad4jNotice> notificationCaptor;

	@Before
	public void setUp() throws Exception {

		hoptoadAppender = new StubHoptoadLogbackAppender();
		hoptoadAppender.start();
	}

	@Test
	public void testAppend() throws IOException {
		when(throwableProxy.getClassName()).thenReturn(THROWABLE.getClass().getName());
		when(throwableProxy.getStackTraceElementProxyArray()).thenReturn(Collections.singletonList(stackTraceElementProxy).toArray(new StackTraceElementProxy[1]));
		when(eventObject.getThrowableProxy()).thenReturn(throwableProxy);
		when(eventObject.getMessage()).thenReturn(MESSAGE);
		hoptoadAppender.append(eventObject);
		verify(hoptoadNotifier).send(notificationCaptor.capture());
		Hoptoad4jNotice notification = notificationCaptor.getValue();
		assertThat(notification.getErrorMessage(), is(MESSAGE));
		assertThat(notification.getThrowableData().getClassName(), is(THROWABLE.getClass().getName()));
	}
}
