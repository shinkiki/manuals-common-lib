package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateEventCommand", description = "이벤트 생성 명령")
public record CreateEventCommand(
        @Schema(description = "이벤트 키(FQCN)") String eventKey,
        @Schema(description = "이벤트명") String name,
        @Schema(description = "이벤트 설명") String eventExpln
) {
    public String trimmedEventKey() {
        return eventKey == null ? null : eventKey.trim();
    }
}
