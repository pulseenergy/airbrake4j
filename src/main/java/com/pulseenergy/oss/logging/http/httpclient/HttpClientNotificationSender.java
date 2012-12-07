package com.pulseenergy.oss.logging.http.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.pulseenergy.oss.logging.http.HttpNotificationSender;

public class HttpClientNotificationSender<T> implements HttpNotificationSender {
	private static final String CHARSET_NAME = "UTF-8";
	private final HttpClient httpClient;
	private final String uri;

	public HttpClientNotificationSender(final String uri, final HttpClient httpClient) {
		this.httpClient = httpClient;
		this.uri = uri;
	}

	@Override
	public void send(final String notification, final String contentType) throws IOException {
		final PostMethod method = new PostMethod(uri);
		method.setRequestEntity(new StringRequestEntity(notification, contentType, CHARSET_NAME));
		final int httpStatus = httpClient.executeMethod(method);
		if (httpStatus != HttpStatus.SC_OK) {
			System.err.printf("ERROR: Received unexpected response code %d from service: %s\n", httpStatus, responseBodyToString(method));
		}
	}

	private String responseBodyToString(final PostMethod method) throws IOException {
		final InputStream stream = method.getResponseBodyAsStream();
		if (stream == null) {
			return "";
		}
		final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		final StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}

}
