package com.skytracker.common.exception;

public class RouteKeyNotFoundException extends BusinessException {
    public RouteKeyNotFoundException(String detail) {
        super(ErrorCode.ROUTE_KEY_NOT_FOUND, detail);
    }
}