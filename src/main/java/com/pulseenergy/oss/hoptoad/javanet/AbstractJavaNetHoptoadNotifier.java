package com.pulseenergy.oss.hoptoad.javanet;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HoptoadNotifier;
import com.pulseenergy.oss.hoptoad.xml.HoptoadDomXmlSerializer;

public abstract class AbstractJavaNetHoptoadNotifier implements HoptoadNotifier {
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final Logger LOGGER = Logger.getLogger(AbstractJavaNetHoptoadNotifier.class.getName());
	private static final int DEFAULT_TIMEOUT = 5000;
	private static final String DEFAULT_HOPTOAD_URI = "http://hoptoadapp.com/notifier_api/v2/notices";
	private final int timeoutInMillis;
	private final HoptoadDomXmlSerializer serializer = new HoptoadDomXmlSerializer();
	private final String hoptoadUri;

	public AbstractJavaNetHoptoadNotifier(final String hoptoadUri, final int timeoutInMillis, final boolean useSSL) {
		this.hoptoadUri = buildHoptoadUri(hoptoadUri, useSSL);
		this.timeoutInMillis = (timeoutInMillis < 1) ? DEFAULT_TIMEOUT : timeoutInMillis;
	}

	private String buildHoptoadUri(final String hoptoadUri, final boolean useSSL) {
		final String uri = isEmpty(hoptoadUri) ? DEFAULT_HOPTOAD_URI : hoptoadUri;
		if (useSSL && uri.startsWith("http://")) {
			return uri.replaceFirst("http://", "https://");
		}
		return uri;
	}

	public void send(final Hoptoad4jNotice notification) throws IOException {
		final String xml = serializer.serialize(notification);
		final HttpURLConnection connection = getHoptoadConnection(hoptoadUri);
		connection.setConnectTimeout(timeoutInMillis);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		OutputStream out = null;
		try {
			out = connection.getOutputStream();
			out.write(xml.getBytes(CHARSET));
		} finally {
			safeClose(out);
		}
		connection.getResponseCode();
		connection.disconnect();
	}

	protected abstract HttpURLConnection getHoptoadConnection(String hoptoadUri) throws IOException;

	protected final static URL urlForString(final String hoptoadUri) {
		URL url;
		try {
			url = new URL(hoptoadUri);
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException("The provided URI is malformed: " + hoptoadUri, e);
		}
		return url;
	}

	private static void safeClose(final Closeable writer) {
		if (writer == null) {
			return;
		}

		try {
			writer.close();
		} catch (final IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to close IO resource", e);
		}
	}

	private static boolean isEmpty(final String value) {
		if (value == null) {
			return true;
		}

		return value.trim().length() == 0;
	}

}
