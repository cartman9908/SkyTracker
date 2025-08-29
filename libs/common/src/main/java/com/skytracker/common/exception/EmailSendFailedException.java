package com.skytracker.common.exception;

public class EmailSendFailedException extends BusinessException {
    public EmailSendFailedException(String detail, Throwable cause) {
        super(ErrorCode.EMAIL_SEND_FAILED, detail, cause);
    }
}