package com.skytracker.common.exception.common;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class RouteAggregationException extends BusinessException {
    public RouteAggregationException(String detail) {
        super(ErrorCode.INVALID_ROUTE_AGGREGATION_FORMAT, detail);
    }
}