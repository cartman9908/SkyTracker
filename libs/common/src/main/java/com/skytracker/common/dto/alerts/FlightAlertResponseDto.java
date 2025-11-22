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

    private Long alertId;                   // 알림 ID (사용자의 개별 알림 식별자)
    private String origin;                  // 출발 공항 코드 (예: ICN)
    private String destination;             // 도착 공항 코드 (예: LHR)
    private String departureDate;           // 출발 날짜 (사용자가 설정한 조건)
    private String returnDate;              // 귀국 날짜 (왕복일 경우만 존재)
    private String airlineCode;             // 항공사 코드 (조건 기반, 특정 항공사 제한 시 사용)
    private String flightNumber;            // 항공편 번호 (특정 편명 알림 설정 시 사용)
    private String travelClass;             // 여행 클래스 (예: ECONOMY, BUSINESS)
    private String currency;                // 사용자가 설정한 통화 기준 (예: KRW)
    private Integer targetPrice;            // 목표 가격 (이 금액 이하일 때 알림 트리거)
    private Integer lastCheckedPrice;       // 마지막 조회된 가격 (최근 검색 결과 스냅샷)
    private boolean isActive;               // 알림 활성 상태 (ON/OFF)
    private boolean nonStop;                // 직항 여부 (true: 직항만, false: 경유 포함)
    private boolean roundTrip;              // 여행 유형 (true: 왕복, false: 편도)

}