package com.skytracker.common.exception;

public class FlightAlertPublishFailed extends BusinessException {
    public FlightAlertPublishFailed(String detail, Throwable cause) {
        super(ErrorCode.FLIGHT_ALERT_PUBLISH_FAILED, detail, cause);
    }
}