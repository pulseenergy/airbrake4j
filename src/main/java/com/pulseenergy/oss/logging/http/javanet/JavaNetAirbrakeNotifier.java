package com.pulseenergy.oss.logging.http.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.logging.NotificationSerializer;

public class JavaNetAirbrakeNotifier extends AbstractJavaNetAirbrakeNotifier<Airbrake4jNotice> {

	public JavaNetAirbrakeNotifier(final String uri, final int timeoutInMillis, final boolean useSSL, final NotificationSerializer<String, Airbrake4jNotice> serializer, final String contentType) {
		super(uri, timeoutInMillis, useSSL, serializer, contentType);
	}

	@Override
	protected HttpURLConnection getHttpConnection(final String uri) throws IOException {
		final URL airbrakeUrl = urlForString(uri);
		return (HttpURLConnection) airbrakeUrl.openConnection();
	}
}
