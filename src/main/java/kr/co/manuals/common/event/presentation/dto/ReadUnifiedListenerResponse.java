package kr.co.manuals.common.event.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 통합 리스너 조회 응답 (DB + 런타임 병합)
 */
public record ReadUnifiedListenerResponse(
        UUID listenerId,
        UUID eventId,
        String eventKey,
        String kind,
        String beanName,
        String beanClass,
        String methodName,
        String useYn,
        String dsctn,
        boolean existsInRuntime,
        boolean existsInDb,
        LocalDateTime regDt,
        LocalDateTime mdfcnDt
) {
}
