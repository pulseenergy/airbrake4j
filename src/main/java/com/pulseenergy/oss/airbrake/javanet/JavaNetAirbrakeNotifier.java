package com.pulseenergy.oss.airbrake.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaNetAirbrakeNotifier extends AbstractJavaNetAirbrakeNotifier {

	public JavaNetAirbrakeNotifier(final String hoptoadUri, final int timeoutInMillis, final boolean useSSL) {
		super(hoptoadUri, timeoutInMillis, useSSL);
	}

	@Override
	protected HttpURLConnection getHoptoadConnection(final String hoptoadUri) throws IOException {
		final URL hoptoadUrl = urlForString(hoptoadUri);
		return (HttpURLConnection) hoptoadUrl.openConnection();
	}
}
