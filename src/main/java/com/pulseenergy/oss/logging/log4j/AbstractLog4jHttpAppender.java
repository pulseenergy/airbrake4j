package com.pulseenergy.oss.logging.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.pulseenergy.oss.logging.NotificationSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationBuilder;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;

public abstract class AbstractLog4jHttpAppender<T> extends AppenderSkeleton {
	private final GuardedAppender guardedAppender = new GuardedAppender();
	private HttpNotificationSender notificationSender;
	private HttpNotificationBuilder<T, LoggingEvent> notificationBuilder;
	private NotificationSerializer<String, T> serializer;

	@Override
	public void activateOptions() {
		this.notificationSender = buildNotificationSender();
		this.notificationBuilder = buildNotificationGenerator();
		this.serializer = buildNotificationSerializer();
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	public void close() {
		// Nothing to do
	}

	protected abstract NotificationSerializer<String, T> buildNotificationSerializer();

	protected abstract HttpNotificationBuilder<T, LoggingEvent> buildNotificationGenerator();

	protected abstract HttpNotificationSender buildNotificationSender();

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
				final String postData = serializer.serialize(notification);
				try {
					notificationSender.send(postData, serializer.getContentType());
				} catch (final Exception e) {
					getErrorHandler().error("Unable to send notification", e, -1, event);
				}
			} finally {
				guard = false;
			}
		}

	}

}
