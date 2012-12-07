package com.pulseenergy.oss.logging.http.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaNetNotificationSender extends AbstractJavaNetNotificationSender {

	public JavaNetNotificationSender(final String uri, final int timeoutInMillis, final boolean useSSL) {
		super(uri, timeoutInMillis, useSSL);
	}

	@Override
	protected HttpURLConnection getHttpConnection(final String uri) throws IOException {
		final URL url = urlForString(uri);
		return (HttpURLConnection) url.openConnection();
	}
}
