package com.pulseenergy.oss.airbrake;

import org.apache.commons.lang.StringUtils;

public class AirbrakeUtil {
	public static final String POST_CONTENT_TYPE = "text/xml; charset=UTF-8";

	private AirbrakeUtil() {
	}

	public static final String DEFAULT_AIRBRAKE_URI = "http://api.airbrake.io/notifier_api/v2/notices";

	public static String getAirbrakeUriOrDefault(final String uri) {
		if (StringUtils.isNotBlank(uri)) {
			return uri;
		}
		return DEFAULT_AIRBRAKE_URI;
	}
}
