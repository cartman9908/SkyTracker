package com.skytracker.dto;

import com.skytracker.common.dto.enums.TripType;
import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
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

    private String outboundDepartureTime;
    private String outboundArrivalTime;

    private String returnDepartureTime;  // 왕복일 때만 존재
    private String returnArrivalTime;    // 왕복일 때만 존재

    private int price;

    public static FlightTicketDto from(FlightSearchResponseDto dto) {

        // 출국 leg (무조건 존재)
        FlightSearchResponseDto.LegDto outbound = dto.getLegs().get(0);

        // 왕복일 경우만 return leg 존재
        FlightSearchResponseDto.LegDto inbound =
                dto.getTripType() == TripType.ROUND_TRIP && dto.getLegs().size() > 1
                        ? dto.getLegs().get(1)
                        : null;

        return FlightTicketDto.builder()
                .departureAirport(outbound.getDepartureAirport())
                .arrivalAirport(outbound.getArrivalAirport())
                .outboundDepartureTime(outbound.getDepartureTime())
                .outboundArrivalTime(outbound.getArrivalTime())
                .returnDepartureTime(inbound != null ? inbound.getDepartureTime() : null)
                .returnArrivalTime(inbound != null ? inbound.getArrivalTime() : null)
                .price(dto.getTotalPrice())
                .build();
    }
}