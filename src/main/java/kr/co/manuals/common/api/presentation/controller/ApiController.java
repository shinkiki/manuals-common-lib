package kr.co.manuals.common.api.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.manuals.common.api.application.ApiService;
import kr.co.manuals.common.api.application.dto.ApiResult;
import kr.co.manuals.common.api.application.export.ApiDocExportService;
import kr.co.manuals.common.api.application.support.SwaggerSuggester;
import kr.co.manuals.common.api.presentation.dto.*;
import kr.co.manuals.common.api.presentation.mapper.ApiPresentationMapper;
import kr.co.manuals.common.layer.presentation.api.common.AbstractPresentationApiController;
import kr.co.manuals.common.layer.presentation.api.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/v1.0/api")
@Tag(name = "Api", description = "API 관리")
@RequiredArgsConstructor
public class ApiController extends AbstractPresentationApiController {
    private final ApiService apiService;
    private final ApiPresentationMapper mapper;
    private final SwaggerSuggester swaggerSuggester;
    private final ApiDocExportService apiDocExportService;

    @Operation(summary = "API 목록 (등록 + 미등록)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ListApiResponse>>> list(
            @RequestParam(value = "changed", required = false) Boolean changed,
            @RequestParam(value = "registered", required = false) Boolean registered
    ) {
        var discovered = apiService.discoverAllWithUnregistered(changed, registered);
        return readAll(mapper.toListResponses(discovered));
    }

    @Operation(summary = "API 단건")
    @GetMapping("/{apiId}")
    public ResponseEntity<ApiResponse<ReadApiResponse>> read(@PathVariable UUID apiId) {
        ApiResult result = apiService.findByApiId(apiId);
        return readOne(mapper.toReadResponse(result));
    }

    @Operation(summary = "API 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<SaveApiResponse>> save(@Valid @RequestBody SaveApiRequest request,
                                                             BindingResult bindingResult) {
        validateBindingResult(bindingResult);
        var command = mapper.toSaveCommand(request);
        var result = apiService.saveApi(command);
        return saveOne(mapper.toSaveResponse(result));
    }

    @Operation(summary = "API 수정")
    @PutMapping("/{apiId}")
    public ResponseEntity<ApiResponse<EditApiResponse>> edit(@PathVariable UUID apiId,
                                                             @Valid @RequestBody EditApiRequest request) {
        var command = mapper.toEditCommand(request);
        var result = apiService.editApi(apiId, command);
        return editOne(mapper.toEditResponse(result));
    }

    @Operation(summary = "API 삭제")
    @DeleteMapping("/{apiId}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable UUID apiId) {
        apiService.deleteApi(apiId);
        return deleteOne();
    }

    @Operation(summary = "API 일괄 삭제")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> deleteBatch(@RequestBody List<UUID> apiIds) {
        apiService.deleteApis(apiIds);
        return deleteAll();
    }

    @Operation(summary = "Swagger 기반 예시 프리뷰", description = "method와 path로 Swagger 예시 Header/Body/Response를 조회합니다.")
    @GetMapping("/preview")
    public ResponseEntity<ApiResponse<PreviewApiResponse>> preview(@RequestParam("method") String method,
                                                                   @RequestParam("path") String path) {
        var ex = swaggerSuggester.suggest(method, path);
        var resp = new PreviewApiResponse(method, path, ex.getHeader(), ex.getBody(), ex.getResponse(), ex.getApiNm(), ex.getApiDsctn());
        return readOne(resp);
    }

    @Operation(summary = "프리뷰 결과로 즉시 저장", description = "필수 필드(methodType, apiNm, apiUrl)만 전달해도 Swagger 예시가 자동 반영되어 저장됩니다.")
    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<SaveApiResponse>> previewSave(@Valid @RequestBody SaveApiRequest request,
                                                                    BindingResult bindingResult) {
        validateBindingResult(bindingResult);
        var command = mapper.toSaveCommand(request);
        var result = apiService.saveApi(command);
        return saveOne(mapper.toSaveResponse(result));
    }

    @Operation(summary = "API 명세서 다운로드", description = "API 목록을 Word(DOCX) 명세서로 내보냅니다.")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportDocx(@RequestParam(value = "format", required = false) String format,
                                             @RequestParam(value = "changed", required = false) Boolean changed,
                                             @RequestParam(value = "registered", required = false) Boolean registered,
                                             @RequestParam(value = "type", required = false) String type) {
        String fmt = (format == null || format.isBlank()) ? "docx" : format.trim().toLowerCase(Locale.ROOT);
        if (!Objects.equals(fmt, "docx")) {
            return ResponseEntity.badRequest().body(("Unsupported format: " + fmt).getBytes());
        }
        byte[] bytes = apiDocExportService.exportDocx(changed, registered, type);
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "api-spec_" + ts + ".docx";
        MediaType contentType = new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(contentType)
                .contentLength(bytes.length)
                .body(bytes);
    }
}
