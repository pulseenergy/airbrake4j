package com.pulseenergy.oss.hoptoad.javanet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.pulseenergy.oss.hoptoad.Hoptoad4jNotice;
import com.pulseenergy.oss.hoptoad.HoptoadNotifier;

@RunWith(MockitoJUnitRunner.class)
public class AbstractJavaNetHoptoadNotifierTest {
	private class StubJavaNetHoptoadNotifier extends AbstractJavaNetHoptoadNotifier {
		public StubJavaNetHoptoadNotifier(final String hoptoadUri, final int timeoutInMillis) {
			super(hoptoadUri, timeoutInMillis);
		}

		@Override
		protected HttpURLConnection getHoptoadConnection(final String uri) {
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
		final HoptoadNotifier notifier = new StubJavaNetHoptoadNotifier("test", expectedTimeout);
		when(httpConnection.getOutputStream()).thenReturn(outputStream);
		notifier.send(new Hoptoad4jNotice());
		verify(httpConnection).setConnectTimeout(expectedTimeout);
		verify(outputStream).write(byteArrayCaptor.capture());
		verify(outputStream).close();
		verify(httpConnection).disconnect();
		final String data = new String(byteArrayCaptor.getValue(), "UTF-8");
		assertThat(data.isEmpty(), is(false));
	}
}
