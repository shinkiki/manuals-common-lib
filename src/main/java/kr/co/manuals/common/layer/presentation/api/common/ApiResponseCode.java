package kr.co.manuals.common.layer.presentation.api.common;

import lombok.Getter;

@Getter
public enum ApiResponseCode {
    SUCCESS(20001, "성공"),
    SAVE(20020, "저장되었습니다."),
    EDIT(20030, "수정되었습니다."),
    DELETE(20040, "삭제되었습니다."),
    VALIDATION_ERROR(40001, "검증오류가 발생했습니다."),
    UNAUTHORIZED(40101, "인증이 필요합니다."),
    NOT_FOUND(40002, "리소스를 찾을 수 없습니다."),
    FORBIDDEN(40301, "리소스 권한이 없습니다."),
    METHOD_NOT_ALLOWED(40501, "지원하지 않는 HTTP 메서드입니다."),
    INTERNAL_ERROR(50001, "서버 오류가 발생했습니다.");

    private final int code;
    private final String message;

    ApiResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
