package com.pulseenergy.oss.logging.http;

import java.io.IOException;

public interface HttpNotificationSender {
	void send(final String notification, final String contentType) throws IOException;
}
