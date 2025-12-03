package kr.co.manuals.common.event.presentation.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 이벤트 생성 요청
 */
public record CreateEventRequest(
        @NotBlank(message = "이벤트 키는 필수입니다")
        String eventKey,

        @NotBlank(message = "이벤트 명은 필수입니다")
        String name,

        String eventExpln,

        String useYn,

        String dispatchType
) {
}
