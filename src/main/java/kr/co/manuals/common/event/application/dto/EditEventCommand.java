package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EditEventCommand", description = "이벤트 수정 명령")
public record EditEventCommand(
        @Schema(description = "이벤트 설명") String eventExpln,
        @Schema(description = "사용여부") String useYn,
        @Schema(description = "디스패치 방식") String dispatchType
) {}
