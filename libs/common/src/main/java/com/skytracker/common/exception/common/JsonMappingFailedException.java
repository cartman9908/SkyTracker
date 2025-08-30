package com.skytracker.common.exception.common;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class JsonMappingFailedException extends BusinessException {
    public JsonMappingFailedException(String detail, Throwable cause) {
        super(ErrorCode.JSON_MAPPING_FAILED, detail, cause);
    }
}