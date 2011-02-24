package com.pulseenergy.oss.hoptoad.log4j;

import com.pulseenergy.oss.hoptoad.HoptoadNotifier;
import com.pulseenergy.oss.hoptoad.javanet.JavaNetHoptoadNotifier;

public class HoptoadLog4jAppender extends AbstractHoptoadLog4jAppender {

	@Override
	protected HoptoadNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL) {
		return new JavaNetHoptoadNotifier(hoptoadUri, timeoutInMillis, useSSL);
	}
}
