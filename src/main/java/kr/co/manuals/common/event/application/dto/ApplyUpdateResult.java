package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "ApplyUpdateResult", description = "선택 갱신 결과 DTO")
public record ApplyUpdateResult(
        @Schema(description = "이벤트 ID") UUID eventId,
        @Schema(description = "생성된 리스너 수") int created,
        @Schema(description = "건너뛴 리스너 수") int skipped,
        @Schema(description = "오류 메시지") String error
) {
    public boolean success() {
        return error == null || error.isBlank();
    }
}
