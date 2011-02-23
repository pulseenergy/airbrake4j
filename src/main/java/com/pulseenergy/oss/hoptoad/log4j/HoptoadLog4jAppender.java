package com.pulseenergy.oss.hoptoad.log4j;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.pulseenergy.oss.hoptoad.HoptoadNotifier;

public class HoptoadLog4jAppender extends AbstractHoptoadLog4jAppender {

	@Override
	protected HoptoadNotifier buildHoptoadNotifier(final String apiKey, final String environment, final long timeoutInMillis, final String hoptoadUri) {
		final HttpClientParams params = new HttpClientParams();
		final HttpClient httpClient = new HttpClient(params);
		return new HoptoadNotifier(httpClient);
	}
}
