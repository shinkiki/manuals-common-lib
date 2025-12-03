package kr.co.manuals.common.event.presentation.dto;

/**
 * 동기화 요청
 */
public record ApplyUpdateRequest(
        boolean syncEvents,
        boolean syncListeners
) {
}
