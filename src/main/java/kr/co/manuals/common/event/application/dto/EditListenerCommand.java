package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EditListenerCommand", description = "DB 리스너 수정 명령")
public record EditListenerCommand(
        @Schema(description = "사용여부") String useYn,
        @Schema(description = "설명") String dsctn
) {}
