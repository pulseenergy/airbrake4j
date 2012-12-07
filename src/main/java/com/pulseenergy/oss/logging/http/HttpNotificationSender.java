package com.pulseenergy.oss.logging.http;

import java.io.IOException;

public interface HttpNotificationSender<T> {
	void send(final T notification) throws IOException;
}