package com.skytracker.common.exception;

public class RouteAggregationException extends BusinessException {
    public RouteAggregationException(String detail) {
        super(ErrorCode.INVALID_ROUTE_AGGREGATION_FORMAT, detail);
    }
}