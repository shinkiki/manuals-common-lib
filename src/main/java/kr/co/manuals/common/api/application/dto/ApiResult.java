package kr.co.manuals.common.api.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiResult(
        UUID apiId,
        String methodType,
        String apiNm,
        String apiDsctn,
        String apiUrl,
        String header,
        String body,
        String response,
        UUID rgtrId,
        String rgtr,
        LocalDateTime regDt,
        UUID mdfrId,
        String mdfr,
        LocalDateTime mdfcnDt
) {}
