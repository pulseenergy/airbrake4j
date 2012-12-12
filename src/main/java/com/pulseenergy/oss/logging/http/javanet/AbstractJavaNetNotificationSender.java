package com.pulseenergy.oss.logging.http.javanet;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pulseenergy.oss.logging.http.HttpNotificationSender;

public abstract class AbstractJavaNetNotificationSender implements HttpNotificationSender {
	private static final String ERR_UNEXPECTED_RESPONSE = "Service responded with an unexpected response code %d:\n%s\n\nSupplied POST data:\n%s";
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final Logger LOGGER = Logger.getLogger(AbstractJavaNetNotificationSender.class.getName());
	private static final int DEFAULT_TIMEOUT = 5000;
	private static final int HTTP_OK = 200;
	private static final String HTTP = "http://";
	private static final String HTTPS = "https://";
	private final int timeoutInMillis;
	private final String uri;

	public AbstractJavaNetNotificationSender(final String uri, final int timeoutInMillis, final boolean useSSL) {
		this.uri = buildNotificationUri(uri, useSSL);
		this.timeoutInMillis = (timeoutInMillis < 1) ? DEFAULT_TIMEOUT : timeoutInMillis;
	}

	@Override
	public void send(final String postData, final String contentType) throws IOException {
		final HttpURLConnection connection = getHttpConnection(uri);
		connection.setConnectTimeout(timeoutInMillis);
		connection.setReadTimeout(timeoutInMillis);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", contentType);
		connection.connect();
		try {
			sendPostToService(postData, connection);
			final int responseCode = connection.getResponseCode();

			if (responseCode != HTTP_OK) {
				final String response = readErrorResponse(connection);
				throw new IOException(String.format(ERR_UNEXPECTED_RESPONSE, responseCode, response, postData));
			}
		} finally {
			connection.disconnect();
		}
	}

	private String buildNotificationUri(final String uri, final boolean useSSL) {
		if (useSSL && uri.startsWith(HTTP)) {
			return uri.replaceFirst(HTTP, HTTPS);
		}
		return uri;
	}

	private String readErrorResponse(final HttpURLConnection connection) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
			String line;
			final StringBuilder buffer = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} finally {
			safeClose(reader);
		}
	}

	private void sendPostToService(final String xml, final HttpURLConnection connection) throws IOException {
		OutputStream out = null;
		try {
			out = connection.getOutputStream();
			out.write(xml.getBytes(CHARSET));
		} finally {
			safeClose(out);
		}
	}

	protected abstract HttpURLConnection getHttpConnection(String uri) throws IOException;

	protected static URL urlForString(final String uri) {
		URL url;
		try {
			url = new URL(uri);
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException("The provided URI is malformed: " + uri, e);
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

}
