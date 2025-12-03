package kr.co.manuals.common.event.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * 이벤트 수정 요청
 */
public record EditEventRequest(
        @NotNull(message = "이벤트 ID는 필수입니다")
        UUID eventId,

        @NotBlank(message = "이벤트 키는 필수입니다")
        String eventKey,

        @NotBlank(message = "이벤트 명은 필수입니다")
        String name,

        String eventExpln,

        String useYn,

        String dispatchType
) {
}
