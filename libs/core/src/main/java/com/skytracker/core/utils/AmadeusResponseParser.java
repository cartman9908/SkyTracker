package com.skytracker.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skytracker.common.dto.SearchContext;
import com.skytracker.common.dto.enums.TripType;
import com.skytracker.common.dto.flightSearch.FlightSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class AmadeusResponseParser {

    private final ObjectMapper objectMapper;

    /**
     * Amadeus 항공권 검색 API 응답을 파싱하여
     * 편도/왕복을 단일 FlightSearchResponseDto 리스트로 변환
     */
    public List<FlightSearchResponseDto> parseFlightSearchResponse(String json, SearchContext context) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode data = root.get("data");
            Map<String, String> carrierMap = extractCarrierMap(root);

            if (data == null || !data.isArray() || data.isEmpty()) {
                throw new RuntimeException("항공권 데이터가 비어있거나 잘못된 형식입니다");
            }

            return StreamSupport.stream(data.spliterator(), false)
                    .map(offer -> toDto(offer, carrierMap, context))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("항공권 응답 파싱 중 오류가 발생했습니다", e);
        }
    }

    // --- 책임1: carrierMap 추출 ---
    private Map<String,String> extractCarrierMap(JsonNode root) {
        return objectMapper.convertValue(
                root.at("/dictionaries/carriers"),
                new TypeReference<Map<String,String>>() {});
    }

    /**
     * JsonNode(offer) → 단일 FlightSearchResponseDto 변환
     * - legs: 편도면 1개, 왕복이면 2개
     */
    private FlightSearchResponseDto toDto(JsonNode offer,
                                          Map<String,String> carrierMap,
                                          SearchContext context) {

        JsonNode itineraries = offer.path("itineraries");
        if (!itineraries.isArray() || itineraries.isEmpty()) {
            throw new RuntimeException("itineraries 가 없거나 잘못된 형식입니다");
        }

        int legsCount = itineraries.size();
        TripType tripType = (legsCount == 1) ? TripType.ONE_WAY : TripType.ROUND_TRIP;

        JsonNode priceNode   = offer.path("price");
        JsonNode fareDetails = offer.at("/travelerPricings/0/fareDetailsBySegment/0");

        String currency   = parseCurrency(priceNode);
        int totalPrice    = parsePriceValue(priceNode);

        boolean hasCheckedBags = parseCheckedBags(fareDetails);
        boolean isRefundable   = parseFlag(fareDetails, "REFUNDABLE");
        boolean isChangeable   = parseFlag(fareDetails, "CHANGEABLE");

        List<FlightSearchResponseDto.LegDto> legs = new ArrayList<>();

        for (int i = 0; i < legsCount; i++) {
            JsonNode itinerary = itineraries.get(i);
            JsonNode segments  = itinerary.path("segments");

            if (!segments.isArray() || segments.isEmpty()) {
                throw new RuntimeException("segments 가 없거나 잘못된 형식입니다 (index: " + i + ")");
            }

            JsonNode firstSeg = segments.get(0);
            JsonNode lastSeg  = segments.get(segments.size() - 1);

            String carrierCode  = firstSeg.path("carrierCode").asText();
            String airlineName  = carrierMap.getOrDefault(carrierCode, "UNKNOWN");
            String flightNumber = firstSeg.path("number").asText();

            // Amadeus 응답 예: departure: { "iataCode": "ICN", "at": "..." }
            String departureAirport = firstSeg.path("departure").path("iataCode")
                    .asText(context.originLocationAirPort());
            String arrivalAirport = lastSeg.path("arrival").path("iataCode")
                    .asText(context.destinationLocationAirPort());

            String departureTime = firstSeg.path("departure").path("at").asText();
            String arrivalTime   = lastSeg.path("arrival").path("at").asText();
            String duration      = itinerary.path("duration").asText();

            int seats = offer.path("numberOfBookableSeats").asInt(0);

            int segCount      = segments.size();
            int numberOfStops = Math.max(0, segCount - 1);
            boolean nonStop   = (numberOfStops == 0);

            FlightSearchResponseDto.LegDto leg = FlightSearchResponseDto.LegDto.builder()
                    .airlineCode(carrierCode)
                    .airlineName(airlineName)
                    .flightNumber(flightNumber)
                    .departureAirport(departureAirport)
                    .departureTime(departureTime)
                    .arrivalAirport(arrivalAirport)
                    .arrivalTime(arrivalTime)
                    .duration(duration)
                    .travelClass(context.travelClass())
                    .numberOfBookableSeats(seats)
                    .nonStop(nonStop)
                    .numberOfStops(numberOfStops)
                    .build();

            legs.add(leg);
        }

        return FlightSearchResponseDto.builder()
                .tripType(tripType)
                .currency(currency)
                .totalPrice(totalPrice)
                .hasCheckedBags(hasCheckedBags)
                .isRefundable(isRefundable)
                .isChangeable(isChangeable)
                .legs(legs)
                .build();
    }

    // --- 책임3: amenities 파싱 및 flag 추출 ---
    private boolean parseFlag(JsonNode fareDetails, String keyword) {
        for (JsonNode amenity : fareDetails.path("amenities")) {
            String desc       = amenity.path("description").asText().toUpperCase();
            boolean chargeable= amenity.path("isChargeable").asBoolean();
            if (!chargeable && desc.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    // --- 책임4: 수하물 포함 여부 ---
    private boolean parseCheckedBags(JsonNode fareDetails) {
        return fareDetails
                .path("includedCheckedBags")
                .path("quantity")
                .asInt(0) > 0;
    }

    // --- 책임5: 통화 단위 파싱 ---
    private String parseCurrency(JsonNode priceNode) {
        return priceNode.path("currency").asText();
    }

    // --- 책임6: 가격 값 파싱 ---
    private int parsePriceValue(JsonNode priceNode) {
        String totalStr = priceNode.path("total").asText("0");
        return (int) Float.parseFloat(totalStr);
    }
}