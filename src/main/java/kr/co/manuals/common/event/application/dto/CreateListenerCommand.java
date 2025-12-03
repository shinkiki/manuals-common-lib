package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateListenerCommand", description = "DB 리스너 생성 명령")
public record CreateListenerCommand(
        @Schema(description = "이벤트 키(FQCN)") String eventKey,
        @Schema(description = "리스너 종류") String kind,
        @Schema(description = "스프링 빈 이름") String beanName,
        @Schema(description = "리스너 클래스 FQCN") String beanClass,
        @Schema(description = "메서드명") String methodName,
        @Schema(description = "사용여부") String useYn,
        @Schema(description = "설명") String dsctn
) {}
