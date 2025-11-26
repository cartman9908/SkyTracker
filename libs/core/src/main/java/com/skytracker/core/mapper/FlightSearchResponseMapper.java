package com.skytracker.core.mapper;

import com.skytracker.common.dto.RouteAggregationDto;
import com.skytracker.common.dto.enums.TravelClass;
import com.skytracker.common.dto.flightSearch.FlightSearchRequestDto;

public class FlightSearchResponseMapper {

    public static FlightSearchRequestDto toFlightSearchRequestDto(RouteAggregationDto route) {
        return FlightSearchRequestDto.builder()
                .originLocationAirport(route.getDepartureAirportCode())
                .destinationLocationAirport(route.getArrivalAirport())
                .departureDate(route.getDepartureDate())
                .returnDate(route.getArrivalDate())
                .adults(route.getAdults())
                .travelClass(TravelClass.ECONOMY)
                .max(20)
                .currencyCode("KRW")
                .build();
    }
}