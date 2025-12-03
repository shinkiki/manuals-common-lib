package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "RuntimeListenerResult", description = "런타임 리스너 조회 DTO")
public record RuntimeListenerResult(
        @Schema(description = "스프링 빈 이름") String beanName,
        @Schema(description = "리스너 클래스 FQCN") String beanClass,
        @Schema(description = "리스너 종류") String kind,
        @Schema(description = "메서드명") String methodName,
        @Schema(description = "이벤트 클래스 FQCN") String eventClass,
        @Schema(description = "파라미터 타입 목록") List<String> paramTypes,
        @Schema(description = "설명") String description
) {}
