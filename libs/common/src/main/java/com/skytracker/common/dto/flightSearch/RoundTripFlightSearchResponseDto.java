package com.skytracker.common.dto.flightSearch;

import com.skytracker.common.dto.enums.TravelClass;
import com.skytracker.common.dto.enums.TripType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundTripFlightSearchResponseDto {

    private String airlineCode;
    private String airlineName;
    private String flightNumber;

    private String departureAirport;
    private String arrivalAirport;
    private String outboundDepartureTime;
    private String outboundArrivalTime;
    private String outboundDuration;

    private String returnDepartureTime;
    private String returnArrivalTime;
    private String returnDuration;

    private TravelClass travelClass;
    private int numberOfBookableSeats;
    private boolean hasCheckedBags;

    private String currency;
    private int price;
    private boolean isRefundable;
    private boolean isChangeable;
    private TripType tripType;

    // 추가: 출국/귀국 각각의 직항/경유 정보
    private boolean outboundNonStop;       // 출국 직항 여부
    private int outboundStops;             // 출국 경유 횟수

    private boolean returnNonStop;         // 귀국 직항 여부
    private int returnStops;               // 귀국 경유 횟수

}
