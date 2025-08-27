package com.skytracker.dto;

import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import com.skytracker.common.dto.flightSearch.RoundTripFlightSearchResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightTicketDto {

    private String departureAirport;
    private String arrivalAirport;
    private String outBoundDepartureTime;
    private String outBoundArrivalTime;
    private String returnDepartureTime;
    private String returnArrivalTime;
    private int price;

    public static FlightTicketDto from(FlightSearchResponseDto dto) {
        return FlightTicketDto.builder()
                .departureAirport(dto.getDepartureAirport())
                .arrivalAirport(dto.getArrivalAirport())
                .outBoundDepartureTime(dto.getDepartureTime())
                .outBoundArrivalTime(dto.getArrivalTime())
                .returnDepartureTime(null)
                .returnArrivalTime(null)
                .price(dto.getPrice())
                .build();
    }

    public static FlightTicketDto from(RoundTripFlightSearchResponseDto dto) {
        return FlightTicketDto.builder()
                .departureAirport(dto.getDepartureAirport())
                .arrivalAirport(dto.getArrivalAirport())
                .outBoundDepartureTime(dto.getOutboundDepartureTime())
                .outBoundArrivalTime(dto.getOutboundArrivalTime())
                .returnDepartureTime(dto.getReturnDepartureTime())
                .returnArrivalTime(dto.getReturnArrivalTime())
                .price(dto.getPrice())
                .build();
    }
}
