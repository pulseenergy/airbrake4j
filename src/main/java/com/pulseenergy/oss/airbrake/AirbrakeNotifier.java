package com.pulseenergy.oss.airbrake;

import com.pulseenergy.oss.http.HttpNotificationSender;


public interface AirbrakeNotifier extends HttpNotificationSender<Airbrake4jNotice> {
}