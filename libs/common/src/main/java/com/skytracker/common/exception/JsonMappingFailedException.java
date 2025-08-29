package com.skytracker.common.exception;

public class JsonMappingFailedException extends BusinessException {
    public JsonMappingFailedException(String detail, Throwable cause) {
        super(ErrorCode.JSON_MAPPING_FAILED, detail, cause);
    }
}