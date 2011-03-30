package com.pulseenergy.oss.hoptoad.log4j;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.pulseenergy.oss.hoptoad.AbstractHoptoadNotificationBuilder;
import com.pulseenergy.oss.hoptoad.ThrowableData;

class Log4jHoptoadNotificationBuilder extends AbstractHoptoadNotificationBuilder<LoggingEvent, ThrowableInformation> {
	Log4jHoptoadNotificationBuilder(final String apiKey, final String environment, final String nodeName, final String componentName) {
		super(apiKey, environment, nodeName, componentName);
	}

	@Override
	protected ThrowableInformation getThrowableDataSource(final LoggingEvent event) {
		return event.getThrowableInformation();
	}

	@Override
	protected String getErrorClassName(final LoggingEvent event) {
		return event.getLoggerName();
	}

	@Override
	protected String getMessage(final LoggingEvent event) {
		return String.valueOf(event.getMessage());
	}

	@Override
	protected ThrowableData extractThrowableData(final ThrowableInformation proxy) {
		return ThrowableData.fromThrowable(proxy.getThrowable());
	}
}
