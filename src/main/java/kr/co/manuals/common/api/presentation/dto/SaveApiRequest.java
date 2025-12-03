package kr.co.manuals.common.api.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public record SaveApiRequest(
        @NotBlank String methodType,
        @NotBlank String apiNm,
        String apiDsctn,
        @NotBlank String apiUrl,
        String header,
        String body,
        String response
) {}
