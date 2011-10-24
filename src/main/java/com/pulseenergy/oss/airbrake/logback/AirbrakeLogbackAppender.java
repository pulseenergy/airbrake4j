package com.pulseenergy.oss.airbrake.logback;

import com.pulseenergy.oss.airbrake.AirbrakeNotifier;
import com.pulseenergy.oss.airbrake.javanet.JavaNetAirbrakeNotifier;

public class AirbrakeLogbackAppender extends AbstractAirbrakeLogbackAppenderBase {
	protected AirbrakeNotifier buildAirbrakeNotifier(final int timeoutInMillis, final String airbrakeUri, final boolean useSSL){
		return new JavaNetAirbrakeNotifier(airbrakeUri, timeoutInMillis, useSSL);
	}
}
