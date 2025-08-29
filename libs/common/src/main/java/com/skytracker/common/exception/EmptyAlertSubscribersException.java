package com.skytracker.common.exception;

public class EmptyAlertSubscribersException extends BusinessException {
    public EmptyAlertSubscribersException() {
        super(ErrorCode.NO_FLIGHT_ALERT_SUBSCRIBERS);
    }
}