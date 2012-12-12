package com.pulseenergy.oss.logging.http.httpclient;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pulseenergy.oss.logging.NotificationSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientNotificationSenderTest {
	private static final String SERIALIZE_MESSAGE = "SERIALIZED";
	private static final String MIME_TYPE = "text/xml";
	private static final String EXPECTED_URL = "http://unit.test/";
	@Mock
	private HttpClient httpClient;
	@Mock
	private NotificationSerializer<String, String> serializer;
	private HttpNotificationSender notificationSender;

	@Before
	public void setUp() {
		notificationSender = new HttpClientNotificationSender(EXPECTED_URL, httpClient);
	}

	@Test
	public void sendErrorNotification() throws Exception {
		when(serializer.serialize(anyString())).thenReturn(SERIALIZE_MESSAGE);
		when(httpClient.execute(isA(HttpPost.class))).thenReturn(buildHttpResponse(HttpStatus.SC_OK));
		notificationSender.send("notification", MIME_TYPE);
	}

	@Test
	public void sendInvalidErrorNotification() throws Exception {
		when(serializer.serialize(anyString())).thenReturn(SERIALIZE_MESSAGE);
		final HttpResponse response = buildHttpResponse(HttpStatus.SC_UNPROCESSABLE_ENTITY);
		when(httpClient.execute(isA(HttpPost.class))).thenReturn(response);
		notificationSender.send("notification", MIME_TYPE);
	}

	private HttpResponse buildHttpResponse(final int statusCode) throws UnsupportedEncodingException {
		final BasicHttpResponse response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("http", 1, 1), statusCode, "SIMULATED RESPONSE"));
		response.setEntity(new StringEntity("SIMULATED BODY"));
		return response;
	}

}
