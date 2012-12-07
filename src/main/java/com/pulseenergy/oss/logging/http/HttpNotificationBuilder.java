package com.pulseenergy.oss.logging.http;

public interface HttpNotificationBuilder<T, U> {
	T build(final U event);
}
