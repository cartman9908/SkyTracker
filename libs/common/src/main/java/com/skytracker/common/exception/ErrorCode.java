package com.skytracker.common.exception;

public enum ErrorCode {

    USER_NOT_FOUND(404, "U404", "해당 유저를 찾을 수 없습니다."),
    ALERT_NOT_FOUND(404, "A404", "알림을 찾을 수 없습니다."),
    ROUTE_KEY_NOT_FOUND(404, "R404", "해당 경로에 대한 랭킹 키를 찾을 수 없습니다."),
    NO_FLIGHT_ALERT_SUBSCRIBERS(404, "A405", "해당 항공권의 구독자를 찾을 수 없습니다."),
    ALERT_ALREADY_REGISTERED(409, "A409", "이미 등록한 알림입니다."),
    INVALID_ROUTE_AGGREGATION_FORMAT(400, "R102", "유효하지 않은 노선 집계 포맷"),
    INVALID_REQUEST(400, "C400", "잘못된 요청입니다."),
    EMAIL_INVALID(400, "M400", "유효하지 않은 이메일 주소입니다."),
    HOT_ROUTE_VALUE_PARSE_FAILED(500, "R101", "HOT_ROUTES 값 파싱 실패"),
    JSON_MAPPING_FAILED(500, "J002", "JSON 역직렬화 실패"),
    FLIGHT_TICKET_SAVE_FAILED(500, "P001", "항공권 결과 저장 실패"),
    FLIGHT_ALERT_PUBLISH_FAILED(502, "K004", "항공권 알림 Kafka 발행 실패"),
    FLIGHT_TICKET_PUBLISH_FAILED(502, "K002", "Kafka 항공권 업데이트 발행 실패"),
    SEARCH_LOG_PUBLISH_FAILED(502, "K001", "검색 로그 Kafka 발행 실패"),
    EMAIL_SEND_FAILED(502, "M001", "메일 발송 중 오류가 발생했습니다."),
    TOKEN_ISSUE_FAILURE(503, "E002", "액세스 토큰 발급 실패"),
    LOCK_TIMEOUT(503, "E003", "분산 락 획득/유지 시간 초과"),
    INTERNAL_ERROR(500, "S500", "내부 서버 오류"),
    FLIGHT_SEARCH_FAILED(502, "F001", "항공권 검색 실패"),
    FLIGHT_PRICE_COMPARISON_FAILED(502, "F002", "항공권 가격 비교 실패");

    public final int httpStatus;
    public final String code;
    public final String message;

    ErrorCode(int httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}