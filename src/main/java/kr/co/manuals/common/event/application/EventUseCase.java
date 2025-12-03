package kr.co.manuals.common.event.application;

import kr.co.manuals.common.event.application.dto.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface EventUseCase {
    List<UnifiedEventResult> listUnifiedEvents();
    EventResult findByEventId(UUID eventId);
    EventResult editEvent(UUID eventId, EditEventCommand command);
    EventResult createEvent(CreateEventCommand command);
    void deleteEvent(UUID eventId);
    void deleteEvents(List<UUID> eventIds);

    List<RuntimeListenerResult> listRuntimeListeners(String eventKeyOrId);
    Map<String, List<RuntimeListenerResult>> listAllRuntimeListenersGrouped();
    List<ScannedEventResult> previewScannedEvents(String sortBy, String order);

    List<DbListenerResult> listDbListeners(UUID eventId);
    List<DbListenerResult> listAllDbListeners();
    DbListenerResult editListener(UUID listenerId, EditListenerCommand command);
    DbListenerResult createListener(CreateListenerCommand command);

    List<ApplyUpdateResult> applyMissingListeners(List<UUID> eventIds);
    List<UnifiedListenerResult> listUnifiedListeners();
    long migrateListenerKindToHandler(boolean dryRun);
}
