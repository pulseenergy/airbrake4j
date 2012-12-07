package com.pulseenergy.oss.logging.http.httpclient;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.pulseenergy.oss.logging.NotificationSerializer;
import com.pulseenergy.oss.logging.http.HttpNotificationSender;

@RunWith(MockitoJUnitRunner.class)
public class HttpClientNotificationSenderTest {
	private static final String SERIALIZE_MESSAGE = "SERIALIZED";

	private final class ExecutePostMethodAnswer implements Answer<Integer> {
		private final int httpStatus;

		public ExecutePostMethodAnswer(final int httpStatus) {
			this.httpStatus = httpStatus;
		}

		public Integer answer(final InvocationOnMock invocation) throws Throwable {
			final PostMethod method = (PostMethod) invocation.getArguments()[0];
			assertProperlyConfiguredMethod(method);
			return httpStatus;
		}
	}

	private static final String EXPECTED_CHARSET_NAME = "UTF-8";
	private static final String EXPECTED_CONTENT_TYPE = "text/xml; charset=" + EXPECTED_CHARSET_NAME;
	private static final String EXPECTED_URL = "http://unit.test/";
	@Mock
	private HttpClient httpClient;
	@Mock
	private NotificationSerializer<String, String> serializer;
	private HttpNotificationSender<String> notificationSender;

	@Before
	public void setUp() {
		notificationSender = new HttpClientNotificationSender<String>(EXPECTED_URL, httpClient, serializer, EXPECTED_CONTENT_TYPE);
	}

	@Test
	public void sendErrorNotification() throws Exception {
		when(serializer.serialize(anyString())).thenReturn(SERIALIZE_MESSAGE);
		when(httpClient.executeMethod(isA(PostMethod.class))).thenAnswer(new ExecutePostMethodAnswer(HttpStatus.SC_OK));
		notificationSender.send("notification");
	}

	@Test
	public void sendInvalidErrorNotification() throws Exception {
		when(serializer.serialize(anyString())).thenReturn(SERIALIZE_MESSAGE);
		when(httpClient.executeMethod(isA(PostMethod.class))).thenAnswer(new ExecutePostMethodAnswer(HttpStatus.SC_UNPROCESSABLE_ENTITY));
		notificationSender.send("notification");
	}

	private static void assertProperlyConfiguredMethod(final PostMethod method) {
		assertThat(method.getRequestEntity() instanceof StringRequestEntity, is(true));
		final StringRequestEntity requestEntity = (StringRequestEntity) method.getRequestEntity();
		assertThat(requestEntity.getContentLength() > 0, is(true));
		assertThat(requestEntity.getContentType(), is(EXPECTED_CONTENT_TYPE));
		assertThat(requestEntity.getCharset(), is(EXPECTED_CHARSET_NAME));

	}

}
