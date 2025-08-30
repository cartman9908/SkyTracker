package com.skytracker.common.exception.kafka;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class FlightTicketSaveFailedException extends BusinessException {
    public FlightTicketSaveFailedException(String detail, Throwable cause) {
        super(ErrorCode.FLIGHT_TICKET_SAVE_FAILED, detail, cause);
    }
}