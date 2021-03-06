package com.pulseenergy.oss.airbrake.log4j;

import static com.pulseenergy.oss.airbrake.AirbrakeUtil.getAirbrakeUriOrDefault;

import org.apache.log4j.spi.LoggingEvent;

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.airbrake.xml.AirbrakeDomXmlSerializer;
import com.pulseenergy.oss.logging.NotificationSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationBuilder;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;
import com.pulseenergy.oss.logging.http.javanet.JavaNetNotificationSender;
import com.pulseenergy.oss.logging.log4j.AbstractLog4jHttpAppender;

public class AirbrakeLog4jAppender extends AbstractLog4jHttpAppender<Airbrake4jNotice> {

	private String apiKey;
	private String environment;
	private int timeoutInMillis;
	private String airbrakeUri;
	private boolean useSSL = false;
	private String nodeName;
	private String componentName;

	protected HttpNotificationSender buildNotificationSender() {
		return new JavaNetNotificationSender(getAirbrakeUriOrDefault(airbrakeUri), timeoutInMillis, useSSL);
	}

	protected HttpNotificationBuilder<Airbrake4jNotice, LoggingEvent> buildNotificationGenerator() {
		return new Log4jAirbrakeNotificationBuilder(apiKey, environment, nodeName, componentName);
	}

	@Override
	protected NotificationSerializer<String, Airbrake4jNotice> buildNotificationSerializer() {
		return new AirbrakeDomXmlSerializer();
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
}
