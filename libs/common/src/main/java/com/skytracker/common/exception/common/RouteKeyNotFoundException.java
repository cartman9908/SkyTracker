package com.skytracker.common.exception.common;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class RouteKeyNotFoundException extends BusinessException {
    public RouteKeyNotFoundException(String detail) {
        super(ErrorCode.ROUTE_KEY_NOT_FOUND, detail);
    }
}