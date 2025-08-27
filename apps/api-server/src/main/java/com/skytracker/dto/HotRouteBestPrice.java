package com.skytracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotRouteBestPrice {

    private String departureAirport;
    private String arrivalAirport;
    private String outBoundDepartureTime;
    private String outBoundArrivalTime;
    private String returnDepartureTime;
    private String returnArrivalTime;
    private int price;

    public static HotRouteBestPrice from(FlightTicketDto dto) {
        return HotRouteBestPrice.builder()
                .departureAirport(dto.getDepartureAirport())
                .arrivalAirport(dto.getArrivalAirport())
                .outBoundDepartureTime(dto.getOutBoundDepartureTime())
                .outBoundArrivalTime(dto.getOutBoundArrivalTime())
                .returnDepartureTime(dto.getReturnDepartureTime())
                .returnArrivalTime(dto.getReturnArrivalTime())
                .price(dto.getPrice())
                .build();
    }
}
