package kr.co.manuals.common.event.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 이벤트 조회 응답
 */
public record ReadEventResponse(
        UUID eventId,
        String eventKey,
        String name,
        String eventExpln,
        String useYn,
        String dispatchType,
        List<UUID> listenerIdList,
        LocalDateTime regDt,
        LocalDateTime mdfcnDt
) {
}
