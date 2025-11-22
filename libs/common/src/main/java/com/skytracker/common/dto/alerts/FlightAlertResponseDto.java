package com.skytracker.common.dto.alerts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightAlertResponseDto {

    private Long alertId;
    private String origin;
    private String destination;
    private String departureDate;
    private String returnDate;
    private String airlineCode;
    private String flightNumber;
    private String travelClass;
    private String currency;
    private Integer targetPrice;
    private Integer lastCheckedPrice;
    private boolean isActive;              // 토클 On/Off
    private boolean nonStop;               // 직항/경유 여부
    private boolean roundTrip;             // 왕복/편도 여부

}