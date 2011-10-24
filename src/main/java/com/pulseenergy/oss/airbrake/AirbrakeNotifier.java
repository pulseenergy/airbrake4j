package com.pulseenergy.oss.airbrake;

import java.io.IOException;


public interface AirbrakeNotifier {

	void send(final Airbrake4jNotice notification) throws IOException;

}