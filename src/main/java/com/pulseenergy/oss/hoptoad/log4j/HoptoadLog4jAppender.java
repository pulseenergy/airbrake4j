package com.pulseenergy.oss.hoptoad.log4j;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.pulseenergy.oss.hoptoad.httpclient.HttpClientHoptoadNotifier;

public class HoptoadLog4jAppender extends AbstractHoptoadLog4jAppender {

	@Override
	protected HttpClientHoptoadNotifier buildHoptoadNotifier(final String apiKey, final String environment, final long timeoutInMillis, final String hoptoadUri) {
		final HttpClientParams params = new HttpClientParams();
		final HttpClient httpClient = new HttpClient(params);
		return new HttpClientHoptoadNotifier(httpClient);
	}
}
