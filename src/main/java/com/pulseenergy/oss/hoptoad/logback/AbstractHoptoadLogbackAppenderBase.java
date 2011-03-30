package com.pulseenergy.oss.hoptoad.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HoptoadNotifier;
import com.pulseenergy.oss.hoptoad.ThrowableData;

public abstract class AbstractHoptoadLogbackAppenderBase extends AppenderBase<ILoggingEvent> {
	private HoptoadNotifier hoptoadNotifier = null;
	private String apiKey;
	private String environment;
	private int timeoutInMillis;
	private String hoptoadUri;
	private boolean useSSL = false;
	private String nodeName;
	private String componentName;

	@Override
	public void start() {
		super.start();
		hoptoadNotifier = buildHoptoadNotifier(timeoutInMillis, hoptoadUri, useSSL);
	}

	protected abstract HoptoadNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL);

	@Override
    protected void append(ILoggingEvent eventObject) {
		final Hoptoad4jNotice notification = buildHoptoadNotification(eventObject);
		try {
			hoptoadNotifier.send(notification);
		} catch (final Exception e) {
			addError("Unable to send notification to Hoptoad", e);
		}

    }

	private Hoptoad4jNotice buildHoptoadNotification(final ILoggingEvent event) {
		final Hoptoad4jNotice notification = new Hoptoad4jNotice();
		notification.setApiKey(apiKey);
		notification.setEnvironmentName(environment);
		final IThrowableProxy throwableInformation = event.getThrowableProxy();
		if (throwableInformation != null) {
			notification.setThrowableData(extractThrowableData(throwableInformation));
		}
		notification.setErrorMessage(event.getMessage());
		notification.setErrorClass(event.getLoggerName());
		notification.setNodeName(nodeName);
		notification.setComponentName(componentName);
		return notification;
	}

	private ThrowableData extractThrowableData(final IThrowableProxy proxy) {
		ThrowableData throwableData = new ThrowableData(proxy.getClassName(), proxy.getMessage());
		for (StackTraceElementProxy elementProxy : proxy.getStackTraceElementProxyArray()) {
			throwableData.addStackTraceElement(elementProxy.getStackTraceElement());
		}
		IThrowableProxy cause = proxy.getCause();
		if (cause != null) {
			throwableData.setCause(extractThrowableData(cause));

		}
		return throwableData;
	}

}
