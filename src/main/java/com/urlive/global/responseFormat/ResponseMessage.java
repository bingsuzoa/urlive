package com.urlive.global.responseFormat;

public enum ResponseMessage {
    LOG_TRAFFIC_DATE_RANGE_SUCCESS("날짜별 유입량 조회 완료"),
    LOG_TRAFFIC_REFERER_SUCCESS("유입 경로 방문자 수 조회 완료"),
    LOG_TRAFFIC_DEVICE_SUCCESS("디바이스별 방문자 수 조회 완료"),
    USER_DUPLICATE_CONFIRM_SUCCESS("회원가입 시 중복 확인 조회 완료"),
    COUNTRIES_GET_SUCCESS("전체 국가 코드 조회 성공"),
    USER_CREATE_SUCCESS("사용자 DB 저장 완료"),
    USER_LOGIN_SUCCESS("사용자 로그인 성공"),
    SHORT_URL_CREATE_SUCCESS("단축 URL 생성 완료"),
    URL_TITLE_UPDATE_SUCCESS("사용자 URL 타이틀 변경 완료"),
    USER_URL_DELETE_SUCCESS("사용자 URL 삭제 완료"),
    USER_PASSWORD_CHANGE_SUCCESS("사용자 비밀번호 변경 완료"),
    URLS_VIEW_SUCCESS("url 목록 조회 성공");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
