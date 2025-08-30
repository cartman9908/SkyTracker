package com.skytracker.common.exception.user;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class EmailInvalidException extends BusinessException {
    public EmailInvalidException(String detail) {
        super(ErrorCode.EMAIL_INVALID, detail);
    }
}