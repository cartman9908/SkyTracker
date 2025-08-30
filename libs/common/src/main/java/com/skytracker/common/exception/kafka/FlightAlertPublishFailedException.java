package com.skytracker.common.exception.kafka;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class FlightAlertPublishFailedException extends BusinessException {
    public FlightAlertPublishFailedException(String detail, Throwable cause) {
        super(ErrorCode.FLIGHT_ALERT_PUBLISH_FAILED, detail, cause);
    }
}