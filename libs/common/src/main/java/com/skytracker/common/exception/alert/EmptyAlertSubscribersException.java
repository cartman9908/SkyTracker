package com.skytracker.common.exception.alert;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class EmptyAlertSubscribersException extends BusinessException {
    public EmptyAlertSubscribersException() {
        super(ErrorCode.NO_FLIGHT_ALERT_SUBSCRIBERS);
    }
}