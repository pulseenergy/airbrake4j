package com.pulseenergy.oss.logging.http.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import com.pulseenergy.oss.logging.http.HttpNotificationSender;

public class HttpClientNotificationSender implements HttpNotificationSender {
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private final HttpClient httpClient;
	private final String uri;

	public HttpClientNotificationSender(final String uri, final HttpClient httpClient) {
		this.httpClient = httpClient;
		this.uri = uri;
	}

	@Override
	public void send(final String notification, final String contentType) throws IOException {
		final HttpPost method = new HttpPost(uri);
		method.setEntity(new StringEntity(notification, ContentType.create(contentType, CHARSET)));
		final HttpResponse httpResponse = httpClient.execute(method);

		final int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			System.err.printf("ERROR: Received unexpected response code %d from service: %s%n", statusCode, responseBodyToString(httpResponse));
		}
	}

	private String responseBodyToString(final HttpResponse response) throws IOException {
		final InputStream stream = response.getEntity().getContent();
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
