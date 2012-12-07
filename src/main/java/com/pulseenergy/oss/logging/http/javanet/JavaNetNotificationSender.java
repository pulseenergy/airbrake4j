package com.pulseenergy.oss.logging.http.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pulseenergy.oss.logging.NotificationSerializer;

public class JavaNetNotificationSender<T> extends AbstractJavaNetNotificationSender<T> {

	public JavaNetNotificationSender(final String uri, final int timeoutInMillis, final boolean useSSL, final NotificationSerializer<String, T> serializer, final String contentType) {
		super(uri, timeoutInMillis, useSSL, serializer, contentType);
	}

	@Override
	protected HttpURLConnection getHttpConnection(final String uri) throws IOException {
		final URL url = urlForString(uri);
		return (HttpURLConnection) url.openConnection();
	}
}
