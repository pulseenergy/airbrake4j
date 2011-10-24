package com.pulseenergy.oss.airbrake.logback;

import com.pulseenergy.oss.airbrake.AirbrakeNotifier;
import com.pulseenergy.oss.airbrake.javanet.JavaNetAirbrakeNotifier;

public class AirbrakeLogbackAppender extends AbstractAirbrakeLogbackAppenderBase {
	protected AirbrakeNotifier buildHoptoadNotifier(final int timeoutInMillis, final String hoptoadUri, final boolean useSSL){
		return new JavaNetAirbrakeNotifier(hoptoadUri, timeoutInMillis, useSSL);
	}
}
