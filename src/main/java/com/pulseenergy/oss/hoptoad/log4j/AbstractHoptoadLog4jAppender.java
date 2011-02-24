package com.pulseenergy.oss.hoptoad.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HoptoadNotifier;

public abstract class AbstractHoptoadLog4jAppender extends AppenderSkeleton {
	private boolean guard = false;
	private HoptoadNotifier hoptoadNotifier;
	private String apiKey;
	private String environment;
	private int timeoutInMillis;
	private String hoptoadUri;

	public AbstractHoptoadLog4jAppender() {
	}

	@Override
	public void activateOptions() {
		this.hoptoadNotifier = buildHoptoadNotifier(timeoutInMillis, hoptoadUri);
	}

	protected abstract HoptoadNotifier buildHoptoadNotifier(int timeoutInMillis, String hoptoadUri);

	@Override
	protected synchronized void append(final LoggingEvent event) {
		if (guard) {
			return;
		}
		guard = true;
		try {
			final Hoptoad4jNotice notification = buildHoptoadNotification(event);
			try {
				hoptoadNotifier.send(notification);
			} catch (final Exception e) {
				getErrorHandler().error("Unable to send notification to Hoptoad", e, -1);
			}
		} finally {
			guard = false;
		}
	}

	public void close() {
	}

	public boolean requiresLayout() {
		return false;
	}

	private Hoptoad4jNotice buildHoptoadNotification(final LoggingEvent event) {
		final Hoptoad4jNotice notification = new Hoptoad4jNotice();
		notification.setApiKey(apiKey);
		notification.setEnvironmentName(environment);
		final ThrowableInformation throwableInformation = event.getThrowableInformation();
		if (throwableInformation != null) {
			notification.populateThrowable(throwableInformation.getThrowable());
		}
		notification.setErrorMessage(event.getMessage().toString());
		return notification;
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

}
