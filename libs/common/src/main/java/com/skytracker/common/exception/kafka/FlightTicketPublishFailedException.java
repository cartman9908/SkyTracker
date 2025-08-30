package com.skytracker.common.exception.kafka;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class FlightTicketPublishFailedException extends BusinessException {
    public FlightTicketPublishFailedException(String detail, Throwable cause) {
        super(ErrorCode.FLIGHT_TICKET_PUBLISH_FAILED, detail, cause);
    }
}