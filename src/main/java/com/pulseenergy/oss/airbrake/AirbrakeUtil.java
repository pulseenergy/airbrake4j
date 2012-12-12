package com.pulseenergy.oss.airbrake;

public class AirbrakeUtil {

	private AirbrakeUtil() {
	}

	public static final String DEFAULT_AIRBRAKE_URI = "http://api.airbrake.io/notifier_api/v2/notices";

	public static String getAirbrakeUriOrDefault(final String uri) {
		if (isNotBlank(uri)) {
			return uri;
		}
		return DEFAULT_AIRBRAKE_URI;
	}

	private static boolean isNotBlank(final String uri) {
		if (uri == null) {
			return false;
		}
		if (uri.trim().length() == 0) {
			return false;
		}

		return true;
	}
}
