package com.pulseenergy.oss.http;

public interface HttpNotificationBuilder<T, U> {
	T build(final U event);
}
