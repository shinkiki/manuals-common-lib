package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ScannedEventResult", description = "스캔된 런타임 이벤트 DTO")
public record ScannedEventResult(
        @Schema(description = "이벤트 키(FQCN)") String eventKey,
        @Schema(description = "이벤트명") String name,
        @Schema(description = "런타임 리스너 수") int listenerCount
) {}
