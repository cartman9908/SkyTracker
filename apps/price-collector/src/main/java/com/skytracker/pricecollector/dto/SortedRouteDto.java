package com.skytracker.pricecollector.dto;

import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortedRouteDto {

    private String departureAirportCode;
    private String arrivalAirportCode;
    private String departureTime; // yyyy-MM-dd
    private String returnDepartureTime;   // 왕복이면 yyyy-MM-dd, 편도면 null
    // 성인 인원수 넣어야하지 않을까?

    public static SortedRouteDto from(FlightSearchResponseDto dto) {

        FlightSearchResponseDto.LegDto outbound = dto.getLegs().get(0);

        FlightSearchResponseDto.LegDto inbound =
                dto.getLegs().size() > 1 ? dto.getLegs().get(1) : null;

        return SortedRouteDto.builder()
                .departureAirportCode(outbound.getDepartureAirport())
                .arrivalAirportCode(outbound.getArrivalAirport())
                .departureTime(outbound.getDepartureTime().substring(0, 10))
                .returnDepartureTime(inbound != null ? inbound.getDepartureTime().substring(0, 10) : null)
                .build();
    }
}