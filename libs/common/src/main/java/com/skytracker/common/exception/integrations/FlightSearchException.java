package com.skytracker.common.exception.integrations;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class FlightSearchException extends BusinessException {
    public FlightSearchException(String detail, Throwable cause) {
        super(ErrorCode.FLIGHT_SEARCH_FAILED, detail, cause);
    }

}
