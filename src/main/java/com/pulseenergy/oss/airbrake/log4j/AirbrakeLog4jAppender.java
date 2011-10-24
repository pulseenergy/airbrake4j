package com.pulseenergy.oss.airbrake.log4j;

import com.pulseenergy.oss.airbrake.AirbrakeNotifier;
import com.pulseenergy.oss.airbrake.javanet.JavaNetAirbrakeNotifier;

public class AirbrakeLog4jAppender extends AbstractAirbrakeLog4jAppender {

	@Override
	protected AirbrakeNotifier buildAirbrakeNotifier(final int timeoutInMillis, final String airbrakeUri, final boolean useSSL) {
		return new JavaNetAirbrakeNotifier(airbrakeUri, timeoutInMillis, useSSL);
	}
}
