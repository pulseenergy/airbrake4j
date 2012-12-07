package com.pulseenergy.oss.airbrake.logback;

import static com.pulseenergy.oss.airbrake.AirbrakeUtil.getAirbrakeUriOrDefault;

import com.pulseenergy.oss.airbrake.xml.AirbrakeDomXmlSerializer;
import com.pulseenergy.oss.logging.NotificationSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;
import com.pulseenergy.oss.logging.http.javanet.JavaNetNotificationSender;
import com.pulseenergy.oss.logging.logback.AbstractLogbackHttpAppenderBase;

public class AirbrakeLogbackAppender extends AbstractLogbackHttpAppenderBase {
	private String apiKey;
	private String environment;
	private int timeoutInMillis;
	private String airbrakeUri;
	private boolean useSSL = false;
	private String nodeName;
	private String componentName;

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

	protected HttpNotificationSender buildNotificationSender(){
		return new JavaNetNotificationSender(getAirbrakeUriOrDefault(airbrakeUri), timeoutInMillis, useSSL);
	}


	@Override
	protected NotificationSerializer buildNotificationSerializer() {
		return new AirbrakeDomXmlSerializer();
	}

	protected LogbackAirbrakeNotificationBuilder buildNotificationGenerator() {
		return new LogbackAirbrakeNotificationBuilder(apiKey, environment, nodeName, componentName);
	}

}
