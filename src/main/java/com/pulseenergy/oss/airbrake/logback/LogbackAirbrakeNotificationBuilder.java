package com.pulseenergy.oss.airbrake.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

import com.pulseenergy.oss.airbrake.AbstractAirbrakeNotificationBuilder;
import com.pulseenergy.oss.airbrake.ThrowableData;

class LogbackAirbrakeNotificationBuilder extends AbstractAirbrakeNotificationBuilder<ILoggingEvent, IThrowableProxy> {
	LogbackAirbrakeNotificationBuilder(final String apiKey, final String environment, final String nodeName, final String componentName) {
		super(apiKey, environment, nodeName, componentName);
	}

	protected IThrowableProxy getThrowableDataSource(final ILoggingEvent event) {
		return event.getThrowableProxy();
	}

	protected String getErrorClassName(final ILoggingEvent event) {
		return event.getLoggerName();
	}

	protected String getMessage(final ILoggingEvent event) {
		return event.getMessage();
	}

	protected ThrowableData extractThrowableData(final IThrowableProxy proxy) {
		ThrowableData throwableData = new ThrowableData(proxy.getClassName(), proxy.getMessage());
		for (StackTraceElementProxy elementProxy : proxy.getStackTraceElementProxyArray()) {
			throwableData.addStackTraceElement(elementProxy.getStackTraceElement());
		}
		IThrowableProxy cause = proxy.getCause();
		if (cause != null) {
			throwableData.setCause(extractThrowableData(cause));

		}
		return throwableData;
	}


}
