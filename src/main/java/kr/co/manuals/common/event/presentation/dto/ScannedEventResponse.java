package kr.co.manuals.common.event.presentation.dto;

/**
 * 스캔된 이벤트 응답
 */
public record ScannedEventResponse(
        String eventKey,
        String className,
        String simpleName,
        boolean registered
) {
}
