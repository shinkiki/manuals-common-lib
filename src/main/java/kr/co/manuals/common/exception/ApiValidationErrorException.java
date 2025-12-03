package kr.co.manuals.common.exception;

import kr.co.manuals.common.layer.presentation.api.common.ApiResponse;
import kr.co.manuals.common.layer.presentation.api.common.ApiResponseCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class ApiValidationErrorException extends RuntimeException {
    private final ApiResponse<Map<String, String>> apiResponse;

    public ApiValidationErrorException(Map<String, String> validationErrors) {
        super(ApiResponseCode.VALIDATION_ERROR.getMessage());
        this.apiResponse = new ApiResponse<>(ApiResponseCode.VALIDATION_ERROR, validationErrors);
    }
}
