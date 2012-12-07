package com.pulseenergy.oss.logging.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.pulseenergy.oss.http.HttpNotificationBuilder;
import com.pulseenergy.oss.http.HttpNotificationSender;

public abstract class AbstractLog4jHttpAppender<T> extends AppenderSkeleton {
	private final GuardedAppender guardedAppender = new GuardedAppender();
	private HttpNotificationSender<T> notificationSender;
	private HttpNotificationBuilder<T, LoggingEvent> notificationBuilder;

	@Override
	public void activateOptions() {
		this.notificationSender = buildNotificationSender();
		this.notificationBuilder = buildNotificationGenerator();
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	public void close() {
		// Nothing to do
	}

	protected abstract HttpNotificationBuilder<T, LoggingEvent> buildNotificationGenerator();

	protected abstract HttpNotificationSender<T> buildNotificationSender();

	@Override
	protected void append(final LoggingEvent event) {
		guardedAppender.append(event);
	}

	private class GuardedAppender {
		private boolean guard = false;

		public synchronized void append(final LoggingEvent event) {
			if (guard) {
				return;
			}
			guard = true;
			try {
				final T notification = notificationBuilder.build(event);
				try {
					notificationSender.send(notification);
				} catch (final Exception e) {
					getErrorHandler().error("Unable to send notification", e, -1, event);
				}
			} finally {
				guard = false;
			}
		}

	}

}
