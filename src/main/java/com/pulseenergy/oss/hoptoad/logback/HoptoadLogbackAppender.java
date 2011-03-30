package com.pulseenergy.oss.hoptoad.logback;

import com.pulseenergy.oss.hoptoad.HoptoadNotifier;
import com.pulseenergy.oss.hoptoad.javanet.JavaNetHoptoadNotifier;

public class HoptoadLogbackAppender extends AbstractHoptoadLogbackAppenderBase {
	protected HoptoadNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL){
		return new JavaNetHoptoadNotifier(hoptoadUri, timeoutInMillis, useSSL);
	}
}
