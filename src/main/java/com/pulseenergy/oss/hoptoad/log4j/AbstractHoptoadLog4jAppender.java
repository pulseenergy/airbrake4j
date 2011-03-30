package com.pulseenergy.oss.hoptoad.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HoptoadNotifier;

public abstract class AbstractHoptoadLog4jAppender extends AppenderSkeleton {
	private HoptoadNotifier hoptoadNotifier = null;
	private String apiKey;
	private String environment;
	private int timeoutInMillis;
	private String hoptoadUri;
	private boolean useSSL = false;
	private final GuardedAppender guardedAppender = new GuardedAppender();
	private String nodeName;
	private String componentName;
	private Log4jHoptoadNotificationBuilder notificationBuilder;

	@Override
	public void activateOptions() {
		this.hoptoadNotifier = buildHoptoadNotifier(timeoutInMillis, hoptoadUri, useSSL);
		this.notificationBuilder = new Log4jHoptoadNotificationBuilder(apiKey, environment, nodeName, componentName);
	}

	protected abstract HoptoadNotifier buildHoptoadNotifier(int timeoutInMillis, String hoptoadUri, boolean useSSL);

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

	public void setHoptoadUri(final String hoptoadUri) {
		this.hoptoadUri = hoptoadUri;
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
				final Hoptoad4jNotice notification = notificationBuilder.build(event);
				try {
					hoptoadNotifier.send(notification);
				} catch (final Exception e) {
					getErrorHandler().error("Unable to send notification to Hoptoad", e, -1);
				}
			} finally {
				guard = false;
			}
		}

	}

}
