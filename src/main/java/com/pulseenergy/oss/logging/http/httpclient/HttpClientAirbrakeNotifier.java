package com.pulseenergy.oss.logging.http.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.airbrake.xml.AirbrakeDomXmlSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;

public class HttpClientAirbrakeNotifier implements HttpNotificationSender<Airbrake4jNotice> {
	private static final String CHARSET_NAME = "UTF-8";
	private static final String CONTENT_TYPE = "text/xml";
	private static final String DEFAULT_AIRBRAKE_URI = "http://airbrakeapp.com/notifier_api/v2/notices";
	private final HttpClient httpClient;
	private final String airbrakeUri;
	private final AirbrakeDomXmlSerializer serializer = new AirbrakeDomXmlSerializer();

	public HttpClientAirbrakeNotifier(final HttpClient httpClient) {
		this(httpClient, null);
	}

	public HttpClientAirbrakeNotifier(final HttpClient httpClient, final String airbrakeUri) {
		this.httpClient = httpClient;
		this.airbrakeUri = StringUtils.isEmpty(airbrakeUri) ? DEFAULT_AIRBRAKE_URI : airbrakeUri;
	}

	public void send(final Airbrake4jNotice notification) throws IOException {
		final PostMethod method = new PostMethod(airbrakeUri);
		method.setRequestEntity(new StringRequestEntity(serializer.serialize(notification), CONTENT_TYPE, CHARSET_NAME));
		final int httpStatus = httpClient.executeMethod(method);
		if (httpStatus != HttpStatus.SC_OK) {
			System.err.printf("ERROR: Received unexpected response code %d from Airbrake: %s\n", httpStatus, responseBodyToString(method));
		}
	}

	private String responseBodyToString(final PostMethod method) throws IOException {
		final InputStream stream = method.getResponseBodyAsStream();
		if (stream == null) {
			return "";
		}
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		final StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}

}
