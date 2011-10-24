package com.pulseenergy.oss.airbrake.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.airbrake.AirbrakeNotifier;

public abstract class AbstractAirbrakeLogbackAppenderBase extends AppenderBase<ILoggingEvent> {
	private AirbrakeNotifier airbrakeNotifier = null;
	private String apiKey;
	private String environment;
	private int timeoutInMillis;
	private String hoptoadUri;
	private boolean useSSL = false;
	private String nodeName;
	private String componentName;
	private LogbackAirbrakeNotificationBuilder notificationBuilder;

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

	@Override
	public void start() {
		super.start();
		airbrakeNotifier = buildHoptoadNotifier(timeoutInMillis, hoptoadUri, useSSL);
		notificationBuilder = new LogbackAirbrakeNotificationBuilder(apiKey, environment, nodeName, componentName);
	}

	protected abstract AirbrakeNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL);

	@Override
    protected void append(ILoggingEvent eventObject) {
		final Airbrake4jNotice notification = notificationBuilder.build(eventObject);
		try {
			airbrakeNotifier.send(notification);
		} catch (final Exception e) {
			addError("Unable to send notification to Hoptoad", e);
		}

    }

}
