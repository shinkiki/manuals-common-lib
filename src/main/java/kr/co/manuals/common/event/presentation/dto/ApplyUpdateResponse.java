package kr.co.manuals.common.event.presentation.dto;

/**
 * 동기화 응답
 */
public record ApplyUpdateResponse(
        int eventsAdded,
        int eventsUpdated,
        int listenersAdded,
        int listenersUpdated,
        int listenersRemoved
) {
}
