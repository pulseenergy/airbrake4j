package com.pulseenergy.oss.hoptoad.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HoptoadNotifier;

public abstract class AbstractHoptoadLogbackAppenderBase extends AppenderBase<ILoggingEvent> {
	private HoptoadNotifier hoptoadNotifier = null;
	private String apiKey;
	private String environment;
	private int timeoutInMillis;
	private String hoptoadUri;
	private boolean useSSL = false;
	private String nodeName;
	private String componentName;
	private LogbackHoptoadNotificationBuilder notificationBuilder;

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
		hoptoadNotifier = buildHoptoadNotifier(timeoutInMillis, hoptoadUri, useSSL);
		notificationBuilder = new LogbackHoptoadNotificationBuilder(apiKey, environment, nodeName, componentName);
	}

	protected abstract HoptoadNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL);

	@Override
    protected void append(ILoggingEvent eventObject) {
		final Hoptoad4jNotice notification = notificationBuilder.build(eventObject);
		try {
			hoptoadNotifier.send(notification);
		} catch (final Exception e) {
			addError("Unable to send notification to Hoptoad", e);
		}

    }

}
