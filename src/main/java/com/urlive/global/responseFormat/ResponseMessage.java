package com.urlive.global.responseFormat;

public enum ResponseMessage {
    USER_CREATE_SUCCESS("사용자 DB 저장 완료"),
    SHORT_KEY_CREATE_SUCCESS("단축 URL 생성 완료"),
    URL_TITLE_UPDATE_SUCCESS("사용자 URL 타이틀 변경 완료"),
    USER_URL_DELETE_SUCCESS("사용자 URL 삭제 완료"),
    URLS_VIEW_SUCCESS("url 목록 조회 성공");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
