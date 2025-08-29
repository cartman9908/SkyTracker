package com.skytracker.common.exception;

public class HotRouteParsedFailed extends BusinessException {
    public HotRouteParsedFailed(String detail) {
        super(ErrorCode.HOT_ROUTE_VALUE_PARSE_FAILED, detail);
    }
}