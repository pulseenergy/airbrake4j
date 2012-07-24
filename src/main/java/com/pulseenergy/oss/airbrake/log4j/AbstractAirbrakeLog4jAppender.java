package com.pulseenergy.oss.airbrake.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.airbrake.AirbrakeNotifier;

public abstract class AbstractAirbrakeLog4jAppender extends AppenderSkeleton {
	private AirbrakeNotifier airbrakeNotifier = null;
	private String apiKey;
	private String environment;
	private int timeoutInMillis;
	private String airbrakeUri;
	private boolean useSSL = false;
	private final GuardedAppender guardedAppender = new GuardedAppender();
	private String nodeName;
	private String componentName;
	private Log4jAirbrakeNotificationBuilder notificationBuilder;

	@Override
	public void activateOptions() {
		this.airbrakeNotifier = buildAirbrakeNotifier(timeoutInMillis, airbrakeUri, useSSL);
		this.notificationBuilder = new Log4jAirbrakeNotificationBuilder(apiKey, environment, nodeName, componentName);
	}

	protected abstract AirbrakeNotifier buildAirbrakeNotifier(int timeoutInMillis, String airbrakeUri, boolean useSSL);

	@Override
	protected void append(final LoggingEvent event) {
		guardedAppender.append(event);
	}

	public void close() {
		// Nothing to do
	}

	public boolean requiresLayout() {
		return false;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	public void setEnvironment(final String environment) {
		this.environment = environment;
	}

	public void setTimeoutInMillis(final int timeoutInMillis) {
		this.timeoutInMillis = timeoutInMillis;
	}

	public void setAirbrakeUri(final String airbrakeUri) {
		this.airbrakeUri = airbrakeUri;
	}

	public void setUseSSL(final boolean useSSL) {
		this.useSSL = useSSL;
	}

	public void setNodeName(final String nodeName) {
		this.nodeName = nodeName;
	}

	public void setComponentName(final String componentName) {
		this.componentName = componentName;
	}

	private class GuardedAppender {
		private boolean guard = false;

		public synchronized void append(final LoggingEvent event) {
			if (guard) {
				return;
			}
			guard = true;
			try {
				final Airbrake4jNotice notification = notificationBuilder.build(event);
				try {
					airbrakeNotifier.send(notification);
				} catch (final Exception e) {
					getErrorHandler().error("Unable to send notification to Airbrake", e, -1);
				}
			} finally {
				guard = false;
			}
		}

	}

}