package com.pulseenergy.oss.airbrake.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaNetAirbrakeNotifier extends AbstractJavaNetAirbrakeNotifier {

	public JavaNetAirbrakeNotifier(final String airbrakeUri, final int timeoutInMillis, final boolean useSSL) {
		super(airbrakeUri, timeoutInMillis, useSSL);
	}

	@Override
	protected HttpURLConnection getAirbrakeConnection(final String airbrakeUri) throws IOException {
		final URL airbrakeUrl = urlForString(airbrakeUri);
		return (HttpURLConnection) airbrakeUrl.openConnection();
	}
}
