package com.skytracker.common.dto.flightSearch;

import com.skytracker.common.dto.enums.TravelClass;
import com.skytracker.common.dto.enums.TripType;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchResponseDto {

    private TripType tripType;          // ONE_WAY / ROUND_TRIP

    private String currency;            // 전체 가격 통화 단위 (예: KRW)
    private int totalPrice;             // 전체 가격 (편도=편도, 왕복=왕복 총액)

    private boolean hasCheckedBags;     // 전체 여정 기준 수하물 포함 여부
    private boolean isRefundable;       // 전체 여정 기준 환불 가능 여부
    private boolean isChangeable;       // 전체 여정 기준 일정 변경 가능 여부

    // 편도면 1개, 왕복이면 2개 (나중에 다구간이면 더 많아져도 됨)
    private List<LegDto> legs;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LegDto {

        private String airlineCode;         // 항공사 코드 (예: KE)
        private String airlineName;         // 항공사 이름 (예: KOREAN AIR)

        private String flightNumber;        // 항공편 번호 (예: 907)

        private String departureAirport;    // 출발 공항 (예: ICN)
        private String departureTime;       // 출발 시간 (예: 2025-07-25T10:55)
        private String arrivalAirport;      // 도착 공항 (예: LHR)
        private String arrivalTime;         // 도착 시간 (예: 2025-07-25T17:20)

        private String duration;            // 해당 leg 총 비행 시간 (예: PT14H25M)

        private TravelClass travelClass;    // 좌석 등급 (예: ECONOMY)
        private int numberOfBookableSeats;  // 예약 가능 좌석 수 (예: 9)

        private boolean nonStop;            // 이 leg 기준 직항(true) / 경유(false)
        private int numberOfStops;          // 이 leg 기준 경유 횟수 (0=직항, 1=1회 경유)
    }
}