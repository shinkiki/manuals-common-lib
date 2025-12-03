package kr.co.manuals.common.layer.presentation.api.common;

import kr.co.manuals.common.exception.ApiValidationErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPresentationApiController {

    protected <P> ResponseEntity<ApiResponse<List<P>>> searchAll(List<P> responseList) {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.SUCCESS, responseList));
    }

    protected <P> ResponseEntity<ApiResponse<P>> readOne(P response) {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.SUCCESS, response));
    }

    protected <P> ResponseEntity<ApiResponse<P>> complete() {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.SUCCESS));
    }

    protected <P> ResponseEntity<ApiResponse<List<P>>> readAll(List<P> responseList) {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.SUCCESS, responseList));
    }

    protected <P> ResponseEntity<ApiResponse<P>> saveOne(P response) {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.SAVE, response));
    }

    protected <P> ResponseEntity<ApiResponse<List<P>>> saveAll(List<P> responseList) {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.SAVE, responseList));
    }

    protected <P> ResponseEntity<ApiResponse<P>> editOne(P response) {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.EDIT, response));
    }

    protected <P> ResponseEntity<ApiResponse<List<P>>> editAll(List<P> responseList) {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.EDIT, responseList));
    }

    protected <P> ResponseEntity<ApiResponse<P>> deleteOne() {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.DELETE));
    }

    protected <P> ResponseEntity<ApiResponse<P>> deleteAll() {
        return ResponseEntity.ok(new ApiResponse<>(ApiResponseCode.DELETE));
    }

    public void validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ApiValidationErrorException(errors);
        }
    }
}
