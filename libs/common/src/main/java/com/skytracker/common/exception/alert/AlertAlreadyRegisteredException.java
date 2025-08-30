package com.skytracker.common.exception.alert;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class AlertAlreadyRegisteredException extends BusinessException {

    public AlertAlreadyRegisteredException() {
        super(ErrorCode.ALERT_ALREADY_REGISTERED);
    }

}
