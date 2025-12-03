package kr.co.manuals.common.event.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "DbListenerResult", description = "DB 리스너 조회 DTO")
public record DbListenerResult(
        @Schema(description = "리스너 ID") UUID listenerId,
        @Schema(description = "이벤트 ID") UUID eventId,
        @Schema(description = "이벤트 키(FQCN)") String eventKey,
        @Schema(description = "리스너 종류") String kind,
        @Schema(description = "스프링 빈 이름") String beanName,
        @Schema(description = "리스너 클래스 FQCN") String beanClass,
        @Schema(description = "메서드명") String methodName,
        @Schema(description = "사용여부") String useYn,
        @Schema(description = "설명") String dsctn,
        @Schema(description = "등록자 ID") UUID rgtrId,
        @Schema(description = "등록자") String rgtr,
        @Schema(description = "등록일시") LocalDateTime regDt,
        @Schema(description = "수정자 ID") UUID mdfrId,
        @Schema(description = "수정자") String mdfr,
        @Schema(description = "수정일시") LocalDateTime mdfcnDt
) {}
