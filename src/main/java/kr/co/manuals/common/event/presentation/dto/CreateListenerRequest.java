package kr.co.manuals.common.event.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * 리스너 생성 요청
 */
public record CreateListenerRequest(
        @NotNull(message = "이벤트 ID는 필수입니다")
        UUID eventId,

        @NotBlank(message = "이벤트 키는 필수입니다")
        String eventKey,

        @NotBlank(message = "구독 종류는 필수입니다")
        String kind,

        @NotBlank(message = "빈명은 필수입니다")
        String beanName,

        @NotBlank(message = "빈클래스는 필수입니다")
        String beanClass,

        @NotBlank(message = "메서드명은 필수입니다")
        String methodName,

        String useYn,

        String dsctn
) {
}
