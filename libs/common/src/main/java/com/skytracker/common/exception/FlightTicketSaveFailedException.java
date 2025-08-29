package com.skytracker.common.exception;

public class FlightTicketSaveFailedException extends BusinessException {
    public FlightTicketSaveFailedException(String detail, Throwable cause) {
        super(ErrorCode.FLIGHT_TICKET_SAVE_FAILED, detail, cause);
    }
}