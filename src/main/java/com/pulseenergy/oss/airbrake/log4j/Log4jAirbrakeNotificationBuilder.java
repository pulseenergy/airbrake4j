package com.pulseenergy.oss.airbrake.log4j;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.pulseenergy.oss.airbrake.AbstractAirbrakeNotificationBuilder;
import com.pulseenergy.oss.airbrake.ThrowableData;

class Log4jAirbrakeNotificationBuilder extends AbstractAirbrakeNotificationBuilder<LoggingEvent, ThrowableInformation> {
	Log4jAirbrakeNotificationBuilder(final String apiKey, final String environment, final String nodeName, final String componentName) {
		super(apiKey, environment, nodeName, componentName);
	}

	@Override
	protected ThrowableInformation getThrowableDataSource(final LoggingEvent event) {
		return event.getThrowableInformation();
	}

	@Override
	protected String getErrorClassName(final LoggingEvent event) {
		if (event.locationInformationExists()) {
			return event.getLocationInformation().getClassName();
		}
		return event.getFQNOfLoggerClass();
	}

	@Override
	protected String getErrorClassMethodName(final LoggingEvent event) {
		if (event.locationInformationExists()) {
			return event.getLocationInformation().getMethodName();
		}
		return "";
	}

	@Override
	protected String getErrorClassLineText(final LoggingEvent event) {
		if (event.locationInformationExists()) {
			return event.getLocationInformation().getLineNumber();
		}
		return "?";
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
