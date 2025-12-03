package kr.co.manuals.common.api.application.dto;

public record EditApiCommand(
        String methodType,
        String apiNm,
        String apiDsctn,
        String apiUrl,
        String header,
        String body,
        String response
) {}
