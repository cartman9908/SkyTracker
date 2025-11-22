package com.skytracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotRouteBestPrice {

    private String departureAirport;
    private String arrivalAirport;

    private String outboundDepartureTime;
    private String outboundArrivalTime;

    private String returnDepartureTime;
    private String returnArrivalTime;

    private int price;

    public static HotRouteBestPrice from(FlightTicketDto dto) {
        return HotRouteBestPrice.builder()
                .departureAirport(dto.getDepartureAirport())
                .arrivalAirport(dto.getArrivalAirport())
                .outboundDepartureTime(dto.getOutboundDepartureTime())
                .outboundArrivalTime(dto.getOutboundArrivalTime())
                .returnDepartureTime(dto.getReturnDepartureTime())
                .returnArrivalTime(dto.getReturnArrivalTime())
                .price(dto.getPrice())
                .build();
    }
}