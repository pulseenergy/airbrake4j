package com.pulseenergy.oss.hoptoad;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

public class HoptoadNotifier {
	private static final String DEFAULT_HOPTOAD_URI = "http://hoptoadapp.com/notifier_api/v2/notices";
	private final HttpClient httpClient;
	private final String hoptoadUri;
	private final HoptoadXmlSerializer serializer = new HoptoadXmlSerializer();

	public HoptoadNotifier(final HttpClient httpClient) {
		this(httpClient, null);
	}

	public HoptoadNotifier(final HttpClient httpClient, final String hoptoadUri) {
		this.httpClient = httpClient;
		this.hoptoadUri = StringUtils.isEmpty(hoptoadUri) ? DEFAULT_HOPTOAD_URI : hoptoadUri;
	}

	public void send(final HoptoadNotification notification) throws IOException {
		final PostMethod method = new PostMethod(hoptoadUri);
		method.setRequestEntity(new StringRequestEntity(serializer.serialize(notification), "text/xml", "UTF-8"));
		final int httpStatus = httpClient.executeMethod(method);
	}

}
