package com.skytracker.common.exception;

public class EmailInvalidException extends BusinessException {
    public EmailInvalidException(String detail) {
        super(ErrorCode.EMAIL_INVALID, detail);
    }
}