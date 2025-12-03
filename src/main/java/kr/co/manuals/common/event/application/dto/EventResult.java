package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "EventResult", description = "이벤트 조회 응답 DTO")
public record EventResult(
        @Schema(description = "이벤트 ID") UUID eventId,
        @Schema(description = "이벤트 키(FQCN)") String eventKey,
        @Schema(description = "이벤트명") String name,
        @Schema(description = "이벤트 설명") String eventExpln,
        @Schema(description = "사용여부") String useYn,
        @Schema(description = "디스패치 방식") String dispatchType,
        @Schema(description = "등록자 ID") UUID rgtrId,
        @Schema(description = "등록자") String rgtr,
        @Schema(description = "등록일시") LocalDateTime regDt,
        @Schema(description = "수정자 ID") UUID mdfrId,
        @Schema(description = "수정자") String mdfr,
        @Schema(description = "수정일시") LocalDateTime mdfcnDt,
        @Schema(description = "DB 리스너 ID 목록") List<UUID> listenerIdList,
        @Schema(description = "등록구분") String registerType,
        @Schema(description = "런타임 리스너 수") Integer runtimeListenerCount,
        @Schema(description = "DB 리스너 수") Integer dbListenerCount
) {}
