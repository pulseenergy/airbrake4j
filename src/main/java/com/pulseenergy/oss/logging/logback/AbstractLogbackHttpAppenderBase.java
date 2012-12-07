package com.pulseenergy.oss.logging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.pulseenergy.oss.logging.http.HttpNotificationBuilder;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;

public abstract class AbstractLogbackHttpAppenderBase<T> extends AppenderBase<ILoggingEvent> {
	private HttpNotificationSender<T> notificationSender;
	private HttpNotificationBuilder<T, ILoggingEvent> notificationBuilder;

	@Override
	public void start() {
		super.start();
		notificationSender = buildNotificationSender();
		notificationBuilder = buildNotificationGenerator();
		startInternal();
	}

	@Override
	public void stop() {
		stopInternal();
		super.stop();
	}

	protected void startInternal() {
	}

	protected abstract HttpNotificationBuilder<T, ILoggingEvent> buildNotificationGenerator();

	protected abstract HttpNotificationSender<T> buildNotificationSender();

	@Override
	protected void append(ILoggingEvent eventObject) {
		final T notification = notificationBuilder.build(eventObject);
		try {
			notificationSender.send(notification);
		} catch (final Exception e) {
			addError("Unable to send notification: " + notification.toString(), e);
		}
	}

	protected void stopInternal() {
	}
}
