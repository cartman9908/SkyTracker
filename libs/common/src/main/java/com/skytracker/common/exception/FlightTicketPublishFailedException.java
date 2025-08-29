package com.skytracker.common.exception;

public class FlightTicketPublishFailedException extends BusinessException {
    public FlightTicketPublishFailedException(String detail, Throwable cause) {
        super(ErrorCode.FLIGHT_TICKET_PUBLISH_FAILED, detail, cause);
    }
}