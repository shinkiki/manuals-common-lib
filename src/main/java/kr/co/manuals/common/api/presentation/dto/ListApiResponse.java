package kr.co.manuals.common.api.presentation.dto;

import java.util.UUID;

public record ListApiResponse(
        UUID apiId,
        String methodType,
        String apiNm,
        String apiDsctn,
        String apiUrl,
        boolean registered,
        String registerType
) {}
