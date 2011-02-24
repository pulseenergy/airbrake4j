package com.pulseenergy.oss.hoptoad.log4j;

import java.io.IOException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HttpClientHoptoadNotifier;

public abstract class AbstractHoptoadLog4jAppender extends AppenderSkeleton {
	private boolean guard = false;
	private HttpClientHoptoadNotifier hoptoadNotifier;
	private String apiKey;
	private String environment;
	private long timeoutInMillis;
	private String hoptoadUri;

	public AbstractHoptoadLog4jAppender() {
	}

	@Override
	public void activateOptions() {
		this.hoptoadNotifier = buildHoptoadNotifier(apiKey, environment, timeoutInMillis, hoptoadUri);
	}

	protected abstract HttpClientHoptoadNotifier buildHoptoadNotifier(String apiKey, String environment, long timeoutInMillis, String hoptoadUri);

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
			} catch (final IOException e) {
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

	public void setTimeoutInMillis(final long timeoutInMillis) {
		this.timeoutInMillis = timeoutInMillis;
	}

	public void setHoptoadUri(final String hoptoadUri) {
		this.hoptoadUri = hoptoadUri;
	}

}
