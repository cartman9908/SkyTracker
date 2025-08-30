package com.skytracker.common.exception.integrations;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class HotRouteParsedFailed extends BusinessException {
    public HotRouteParsedFailed(String detail) {
        super(ErrorCode.HOT_ROUTE_VALUE_PARSE_FAILED, detail);
    }
}