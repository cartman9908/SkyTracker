package com.skytracker.common.exception.alert;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class FlightAlertNotFoundException extends BusinessException {

    public FlightAlertNotFoundException(Long alertId) {
        super(ErrorCode.ALERT_NOT_FOUND, "alertId=" + alertId);
    }

    public FlightAlertNotFoundException() {
        super(ErrorCode.ALERT_NOT_FOUND);
    }

}
