package kr.co.manuals.common.api.presentation.dto;

public record PreviewApiResponse(
        String methodType,
        String apiUrl,
        String header,
        String body,
        String response,
        String apiNm,
        String apiDsctn
) {}
