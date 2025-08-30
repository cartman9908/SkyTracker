package com.skytracker.common.exception.user;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class EmailSendFailedException extends BusinessException {
    public EmailSendFailedException(String detail, Throwable cause) {
        super(ErrorCode.EMAIL_SEND_FAILED, detail, cause);
    }
}