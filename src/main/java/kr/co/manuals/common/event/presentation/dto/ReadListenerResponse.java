package kr.co.manuals.common.event.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 런타임 리스너 조회 응답
 */
public record ReadListenerResponse(
        UUID listenerId,
        UUID eventId,
        String eventKey,
        String kind,
        String beanName,
        String beanClass,
        String methodName,
        String useYn,
        String dsctn,
        LocalDateTime regDt,
        LocalDateTime mdfcnDt
) {
}
