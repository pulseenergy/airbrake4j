package com.pulseenergy.oss.hoptoad.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaNetHoptoadNotifier extends AbstractJavaNetHoptoadNotifier {

	public JavaNetHoptoadNotifier(final String hoptoadUri, final int timeoutInMillis) {
		super(hoptoadUri, timeoutInMillis);
	}

	@Override
	protected HttpURLConnection getHoptoadConnection(final String hoptoadUri) throws IOException {
		final URL hoptoadUrl = urlForString(hoptoadUri);
		return (HttpURLConnection) hoptoadUrl.openConnection();
	}
}
