package kr.co.manuals.common.event.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.manuals.common.event.application.EventUseCase;
import kr.co.manuals.common.event.application.dto.*;
import kr.co.manuals.common.event.presentation.dto.*;
import kr.co.manuals.common.event.presentation.mapper.EventPresentationMapper;
import kr.co.manuals.common.layer.presentation.api.common.AbstractPresentationApiController;
import kr.co.manuals.common.layer.presentation.api.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 시스템 이벤트 관리 컨트롤러
 */
@Tag(name = "시스템 이벤트 관리", description = "시스템 이벤트 및 리스너 관리 API")
@RestController
@RequestMapping("/v1.0/system/events")
public class SystemEventController extends AbstractPresentationApiController {

    private final EventUseCase eventUseCase;
    private final EventPresentationMapper mapper;

    public SystemEventController(EventUseCase eventUseCase, EventPresentationMapper mapper) {
        this.eventUseCase = eventUseCase;
        this.mapper = mapper;
    }

    // ============ 이벤트 CRUD ============

    @Operation(summary = "통합 이벤트 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadEventResponse>>> getEvents() {
        List<UnifiedEventResult> results = eventUseCase.listUnifiedEvents();
        return readAll(mapper.toEventResponseListFromUnified(results));
    }

    @Operation(summary = "이벤트 단건 조회")
    @GetMapping("/{eventId}")
    public ResponseEntity<ApiResponse<ReadEventResponse>> getEvent(@PathVariable UUID eventId) {
        EventResult result = eventUseCase.findByEventId(eventId);
        return readOne(mapper.toResponse(result));
    }

    @Operation(summary = "이벤트 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<ReadEventResponse>> createEvent(@RequestBody CreateEventRequest request) {
        EventResult result = eventUseCase.createEvent(mapper.toCommand(request));
        return saveOne(mapper.toResponse(result));
    }

    @Operation(summary = "이벤트 수정")
    @PutMapping("/{eventId}")
    public ResponseEntity<ApiResponse<ReadEventResponse>> updateEvent(
            @PathVariable UUID eventId,
            @RequestBody EditEventRequest request) {
        EventResult result = eventUseCase.editEvent(eventId, mapper.toCommand(request));
        return editOne(mapper.toResponse(result));
    }

    @Operation(summary = "이벤트 삭제")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable UUID eventId) {
        eventUseCase.deleteEvent(eventId);
        return deleteOne();
    }

    // ============ 스캔된 이벤트 ============

    @Operation(summary = "런타임 스캔 이벤트 목록")
    @GetMapping("/scanned")
    public ResponseEntity<ApiResponse<List<ScannedEventResponse>>> getScannedEvents(
            @RequestParam(defaultValue = "eventKey") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        List<ScannedEventResult> results = eventUseCase.previewScannedEvents(sortBy, order);
        return readAll(mapper.toScannedResponseList(results));
    }

    // ============ 리스너 CRUD ============

    @Operation(summary = "이벤트별 DB 리스너 목록 조회")
    @GetMapping("/{eventId}/listeners")
    public ResponseEntity<ApiResponse<List<ReadDbListenerResponse>>> getListeners(@PathVariable UUID eventId) {
        List<DbListenerResult> results = eventUseCase.listDbListeners(eventId);
        return readAll(mapper.toDbListenerResponseList(results));
    }

    @Operation(summary = "런타임 리스너 목록 조회")
    @GetMapping("/listeners/runtime")
    public ResponseEntity<ApiResponse<List<ReadListenerResponse>>> getRuntimeListeners(
            @RequestParam(required = false) String eventKeyOrId) {
        List<RuntimeListenerResult> results = eventUseCase.listRuntimeListeners(eventKeyOrId);
        return readAll(mapper.toListenerResponseList(results));
    }

    @Operation(summary = "통합 리스너 목록 조회 (DB + 런타임 병합)")
    @GetMapping("/listeners/unified")
    public ResponseEntity<ApiResponse<List<ReadUnifiedListenerResponse>>> getUnifiedListeners() {
        List<UnifiedListenerResult> results = eventUseCase.listUnifiedListeners();
        return readAll(mapper.toUnifiedResponseList(results));
    }

    @Operation(summary = "리스너 생성")
    @PostMapping("/{eventId}/listeners")
    public ResponseEntity<ApiResponse<ReadDbListenerResponse>> createListener(
            @PathVariable UUID eventId,
            @RequestBody CreateListenerRequest request) {
        DbListenerResult result = eventUseCase.createListener(mapper.toCommand(request));
        return saveOne(mapper.toDbListenerResponse(result));
    }

    @Operation(summary = "리스너 수정")
    @PutMapping("/{eventId}/listeners/{listenerId}")
    public ResponseEntity<ApiResponse<ReadDbListenerResponse>> updateListener(
            @PathVariable UUID eventId,
            @PathVariable UUID listenerId,
            @RequestBody EditListenerRequest request) {
        DbListenerResult result = eventUseCase.editListener(listenerId, mapper.toCommand(request));
        return editOne(mapper.toDbListenerResponse(result));
    }

    // ============ 동기화 ============

    @Operation(summary = "누락된 리스너 추가 동기화")
    @PostMapping("/sync")
    public ResponseEntity<ApiResponse<List<ApplyUpdateResponse>>> syncWithRuntime(
            @RequestBody(required = false) List<UUID> eventIds) {
        List<ApplyUpdateResult> results = eventUseCase.applyMissingListeners(eventIds);
        return saveOne(mapper.toApplyResponseList(results));
    }
}
