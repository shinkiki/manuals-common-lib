package kr.co.manuals.common.api.presentation.dto;

public record EditApiRequest(
        String methodType,
        String apiNm,
        String apiDsctn,
        String apiUrl,
        String header,
        String body,
        String response
) {}
