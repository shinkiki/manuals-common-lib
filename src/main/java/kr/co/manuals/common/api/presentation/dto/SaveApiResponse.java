package kr.co.manuals.common.api.presentation.dto;

import java.util.UUID;

public record SaveApiResponse(
        UUID apiId,
        String methodType,
        String apiNm,
        String apiDsctn,
        String apiUrl,
        String header,
        String body,
        String response
) {}
