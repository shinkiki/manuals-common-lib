package kr.co.manuals.common.api.application.dto;

import java.util.UUID;

public record ApiDiscoveryResult(
        UUID apiId,
        String methodType,
        String apiNm,
        String apiDsctn,
        String apiUrl,
        boolean registered,
        boolean changed,
        String registerType
) {}
