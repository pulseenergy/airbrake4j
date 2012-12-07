package com.pulseenergy.oss.airbrake;

import com.pulseenergy.oss.logging.http.HttpNotificationBuilder;

public abstract class AbstractAirbrakeNotificationBuilder<T, U> implements HttpNotificationBuilder<Airbrake4jNotice, T> {
	private final String apiKey;
	private final String environment;
	private final String nodeName;
	private final String componentName;

	protected AbstractAirbrakeNotificationBuilder(final String apiKey, final String environment, final String nodeName, final String componentName) {
		this.apiKey = apiKey;
		this.environment = environment;
		this.nodeName = nodeName;
		this.componentName = componentName;
	}

	@Override
	public final Airbrake4jNotice build(final T event) {
		final Airbrake4jNotice notification = new Airbrake4jNotice();
		notification.setApiKey(apiKey);
		notification.setEnvironmentName(environment);
		final U throwableInformation = getThrowableDataSource(event);
		if (throwableInformation != null) {
			notification.setThrowableData(extractThrowableData(throwableInformation));
		}
		notification.setErrorMessage(getMessage(event));
		notification.setErrorClass(getErrorClassName(event));
		notification.setErrorClassLineText(getErrorClassLineText(event));
		notification.setErrorClassMethodName(getErrorClassMethodName(event));
		notification.setNodeName(nodeName);
		notification.setComponentName(componentName);
		return notification;
	}


	protected abstract U getThrowableDataSource(final T event);

	protected abstract String getErrorClassName(final T event);

	protected abstract String getErrorClassMethodName(final T event);

	protected abstract String getErrorClassLineText(final T event);

	protected abstract String getMessage(final T event);

	protected abstract ThrowableData extractThrowableData(final U proxy);


}
