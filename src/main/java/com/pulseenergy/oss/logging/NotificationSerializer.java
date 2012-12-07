package com.pulseenergy.oss.logging;

public interface NotificationSerializer<T, U> {
	T serialize(final U notification);

	String getContentType();
}
