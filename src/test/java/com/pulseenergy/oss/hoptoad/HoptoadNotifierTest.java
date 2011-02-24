package com.pulseenergy.oss.hoptoad;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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

@RunWith(MockitoJUnitRunner.class)
public class HoptoadNotifierTest {
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
	private HttpClientHoptoadNotifier hoptoadNotifier;

	@Before
	public void setUp() {
		hoptoadNotifier = new HttpClientHoptoadNotifier(httpClient, EXPECTED_URL);
	}

	@Test
	public void sendErrorNotification() throws Exception {
		final Hoptoad4jNotice notification = new Hoptoad4jNotice();
		when(httpClient.executeMethod(isA(PostMethod.class))).thenAnswer(new ExecutePostMethodAnswer(HttpStatus.SC_OK));
		hoptoadNotifier.send(notification);
	}

	@Test
	public void sendInvalidErrorNotification() throws Exception {
		final Hoptoad4jNotice notification = new Hoptoad4jNotice();
		when(httpClient.executeMethod(isA(PostMethod.class))).thenAnswer(new ExecutePostMethodAnswer(HttpStatus.SC_UNPROCESSABLE_ENTITY));
		hoptoadNotifier.send(notification);
	}

	private static void assertProperlyConfiguredMethod(final PostMethod method) {
		assertThat(method.getRequestEntity() instanceof StringRequestEntity, is(true));
		final StringRequestEntity requestEntity = (StringRequestEntity) method.getRequestEntity();
		assertThat(requestEntity.getContentLength() > 0, is(true));
		assertThat(requestEntity.getContentType(), is(EXPECTED_CONTENT_TYPE));
		assertThat(requestEntity.getCharset(), is(EXPECTED_CHARSET_NAME));

	}

}
