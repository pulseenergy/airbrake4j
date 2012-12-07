package com.pulseenergy.oss.logging.http.javanet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pulseenergy.oss.airbrake.Airbrake4jNotice;
import com.pulseenergy.oss.airbrake.xml.AirbrakeDomXmlSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;

@RunWith(MockitoJUnitRunner.class)
public class AbstractJavaNetAirbrakeNotifierTest {

	private static final String URI = "http://this.is.a.test";

	private class StubJavaNetNotifier extends AbstractJavaNetAirbrakeNotifier<Airbrake4jNotice> {
		public StubJavaNetNotifier(final String uri, final int timeoutInMillis) {
			super(uri, timeoutInMillis, true, new AirbrakeDomXmlSerializer(), "text/xml; charset=UTF-8");
		}

		@Override
		protected HttpURLConnection getHttpConnection(final String uri) {
			return httpConnection;
		}

	}

	@Mock
	private HttpURLConnection httpConnection;
	@Mock
	private OutputStream outputStream;
	@Captor
	private ArgumentCaptor<byte[]> byteArrayCaptor;

	@Test
	public void send() throws Exception {
		final int expectedTimeout = 1999;
		final HttpNotificationSender<Airbrake4jNotice> notifier = new StubJavaNetNotifier(URI, expectedTimeout);
		when(httpConnection.getOutputStream()).thenReturn(outputStream);
		when(httpConnection.getResponseCode()).thenReturn(200);
		notifier.send(new Airbrake4jNotice());
		verify(httpConnection).setConnectTimeout(expectedTimeout);
		verify(httpConnection).setReadTimeout(expectedTimeout);
		verify(outputStream).write(byteArrayCaptor.capture());
		verify(outputStream).close();
		verify(httpConnection).disconnect();
		final String data = new String(byteArrayCaptor.getValue(), "UTF-8");
		assertThat(data.isEmpty(), is(false));
	}

	@Test(expected = IOException.class)
	public void sendResultingInFailureFromAirbrake() throws Exception {
		final HttpNotificationSender<Airbrake4jNotice> notifier = new StubJavaNetNotifier(URI, 1999);
		when(httpConnection.getOutputStream()).thenReturn(outputStream);
		when(httpConnection.getResponseCode()).thenReturn(413);
		when(httpConnection.getErrorStream()).thenReturn(new ByteArrayInputStream("THIS IS THE RESPONSE".getBytes()));
		notifier.send(new Airbrake4jNotice());
	}
}
