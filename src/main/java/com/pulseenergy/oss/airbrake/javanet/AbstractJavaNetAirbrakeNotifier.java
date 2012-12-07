package com.pulseenergy.oss.airbrake.javanet;

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

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.airbrake.xml.AirbrakeDomXmlSerializer;
import com.pulseenergy.oss.http.HttpNotificationSender;

public abstract class AbstractJavaNetAirbrakeNotifier implements HttpNotificationSender<Airbrake4jNotice> {
	private static final String ERR_UNEXPECTED_RESPONSE = "Airbrake responded with an unexpected response code %d:\n%s\n\nSupplied XML:\n%s";
	private static final Charset CHARSET = Charset.forName("UTF-8");
	private static final Logger LOGGER = Logger.getLogger(AbstractJavaNetAirbrakeNotifier.class.getName());
	private static final int DEFAULT_TIMEOUT = 5000;
	private static final String DEFAULT_AIRBRAKE_URI = "http://api.airbrake.io/notifier_api/v2/notices";
	private static final int HTTP_OK = 200;
	private final int timeoutInMillis;
	private final AirbrakeDomXmlSerializer serializer = new AirbrakeDomXmlSerializer();
	private final String airbrakeUri;

	public AbstractJavaNetAirbrakeNotifier(final String airbrakeUri, final int timeoutInMillis, final boolean useSSL) {
		this.airbrakeUri = buildAirbrakeUri(airbrakeUri, useSSL);
		this.timeoutInMillis = (timeoutInMillis < 1) ? DEFAULT_TIMEOUT : timeoutInMillis;
	}

	private String buildAirbrakeUri(final String airbrakeUri, final boolean useSSL) {
		final String uri = isEmpty(airbrakeUri) ? DEFAULT_AIRBRAKE_URI : airbrakeUri;
		if (useSSL && uri.startsWith("http://")) {
			return uri.replaceFirst("http://", "https://");
		}
		return uri;
	}

	public void send(final Airbrake4jNotice notification) throws IOException {
		final String xml = serializer.serialize(notification);
		final HttpURLConnection connection = getAirbrakeConnection(airbrakeUri);
		connection.setConnectTimeout(timeoutInMillis);
		connection.setReadTimeout(timeoutInMillis);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
		connection.connect();
		try {
			writeXmlToAirbrake(xml, connection);
			final int responseCode = connection.getResponseCode();

			if (responseCode != HTTP_OK) {
				final String response = readAirbrakeErrorResponse(connection);
				throw new IOException(String.format(ERR_UNEXPECTED_RESPONSE, responseCode, response, xml));
			}
		} finally {
			connection.disconnect();
		}
	}

	private String readAirbrakeErrorResponse(final HttpURLConnection connection) throws IOException {
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

	private void writeXmlToAirbrake(final String xml, final HttpURLConnection connection) throws IOException {
		OutputStream out = null;
		try {
			out = connection.getOutputStream();
			out.write(xml.getBytes(CHARSET));
		} finally {
			safeClose(out);
		}
	}

	protected abstract HttpURLConnection getAirbrakeConnection(String airbrakeUri) throws IOException;

	protected static URL urlForString(final String airbrakeUri) {
		URL url;
		try {
			url = new URL(airbrakeUri);
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException("The provided URI is malformed: " + airbrakeUri, e);
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
