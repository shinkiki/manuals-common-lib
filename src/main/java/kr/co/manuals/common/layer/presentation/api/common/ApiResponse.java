package kr.co.manuals.common.layer.presentation.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
    @Schema(description = "응답코드")
    private final int code;
    @Schema(description = "응답메시지")
    private final String message;
    @Schema(description = "응답데이터")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data = null;

    public ApiResponse(ApiResponseCode responseCode, String message) {
        this.code = responseCode.getCode();
        this.message = message;
    }
    public ApiResponse(ApiResponseCode responseCode, String message, T data) {
        this.code = responseCode.getCode();
        this.message = message;
        this.data = data;
    }
    public ApiResponse(ApiResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }
    public ApiResponse(ApiResponseCode responseCode, T data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }
}
