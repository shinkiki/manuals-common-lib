package kr.co.manuals.common.event.presentation.mapper;

import kr.co.manuals.common.event.application.dto.*;
import kr.co.manuals.common.event.presentation.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * 이벤트 Presentation 매퍼
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventPresentationMapper {

    // Request -> Command
    CreateEventCommand toCommand(CreateEventRequest request);
    EditEventCommand toCommand(EditEventRequest request);
    CreateListenerCommand toCommand(CreateListenerRequest request);
    EditListenerCommand toCommand(EditListenerRequest request);

    // Result -> Response
    ReadEventResponse toResponse(EventResult result);
    List<ReadEventResponse> toEventResponseList(List<EventResult> results);

    // UnifiedEventResult -> ReadEventResponse
    @Mapping(target = "listenerIdList", ignore = true)
    ReadEventResponse toResponseFromUnified(UnifiedEventResult result);
    List<ReadEventResponse> toEventResponseListFromUnified(List<UnifiedEventResult> results);

    ScannedEventResponse toScannedResponse(ScannedEventResult result);
    List<ScannedEventResponse> toScannedResponseList(List<ScannedEventResult> results);

    ReadListenerResponse toListenerResponse(RuntimeListenerResult result);
    List<ReadListenerResponse> toListenerResponseList(List<RuntimeListenerResult> results);

    ReadDbListenerResponse toDbListenerResponse(DbListenerResult result);
    List<ReadDbListenerResponse> toDbListenerResponseList(List<DbListenerResult> results);

    ReadUnifiedListenerResponse toUnifiedResponse(UnifiedListenerResult result);
    List<ReadUnifiedListenerResponse> toUnifiedResponseList(List<UnifiedListenerResult> results);

    ApplyUpdateResponse toApplyResponse(ApplyUpdateResult result);
    List<ApplyUpdateResponse> toApplyResponseList(List<ApplyUpdateResult> results);
}
