package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "UnifiedListenerResult", description = "통합 리스너 메타 DTO")
public record UnifiedListenerResult(
        @Schema(description = "리스너 ID(DB)") UUID listenerId,
        @Schema(description = "이벤트 ID(DB)") UUID eventId,
        @Schema(description = "이벤트 키(FQCN)") String eventKey,
        @Schema(description = "이벤트명") String eventName,
        @Schema(description = "리스너 종류") String kind,
        @Schema(description = "스프링 빈 이름") String beanName,
        @Schema(description = "리스너 클래스 FQCN") String beanClass,
        @Schema(description = "메서드명") String methodName,
        @Schema(description = "사용여부") String useYn,
        @Schema(description = "등록구분") String registerType,
        @Schema(description = "설명") String dsctn
) {}
