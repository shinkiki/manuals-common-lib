package kr.co.manuals.common.event.application.impl;

import jakarta.persistence.EntityNotFoundException;
import kr.co.manuals.common.event.application.EventService;
import kr.co.manuals.common.event.application.dto.*;
import kr.co.manuals.common.event.application.mapper.EventApplicationMapper;
import kr.co.manuals.common.event.EventRuntimeScanner;
import kr.co.manuals.common.event.domain.Event;
import kr.co.manuals.common.event.domain.EventListener;
import kr.co.manuals.common.event.domain.port.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventReadPort eventReadPort;
    private final EventSavePort eventSavePort;
    private final EventDeletePort eventDeletePort;
    private final EventListenerReadPort listenerReadPort;
    private final EventListenerSavePort listenerSavePort;
    private final EventApplicationMapper mapper;
    private final EventRuntimeScanner runtimeScanner;

    @Override
    @Transactional(readOnly = true)
    public List<UnifiedEventResult> listUnifiedEvents() {
        List<Event> dbEvents = eventReadPort.findAll();
        Map<String, List<RuntimeListenerResult>> runtimeMap = runtimeScanner.scanAllListeners();
        
        // DB 이벤트 + 런타임 이벤트 통합
        Map<String, UnifiedEventResult> resultMap = new LinkedHashMap<>();
        
        // DB 이벤트 추가
        for (Event event : dbEvents) {
            List<EventListener> dbListeners = listenerReadPort.findByEventId(event.getEventId());
            List<RuntimeListenerResult> runtimeListeners = runtimeMap.getOrDefault(event.getEventKey(), Collections.emptyList());
            
            UnifiedEventResult result = new UnifiedEventResult(
                    event.getEventId(),
                    event.getEventKey(),
                    event.getName(),
                    event.getEventExpln(),
                    event.getUseYn(),
                    event.getDispatchType() != null ? event.getDispatchType().name() : "SYNC",
                    event.getRgtrId(),
                    event.getRgtr(),
                    event.getRegDt(),
                    event.getMdfrId(),
                    event.getMdfr(),
                    event.getMdfcnDt(),
                    dbListeners.stream().map(EventListener::getListenerId).toList(),
                    "등록",
                    runtimeListeners.size(),
                    dbListeners.size()
            );
            resultMap.put(event.getEventKey(), result);
        }
        
        // 런타임에만 있는 이벤트 추가
        for (String eventKey : runtimeMap.keySet()) {
            if (!resultMap.containsKey(eventKey)) {
                List<RuntimeListenerResult> listeners = runtimeMap.get(eventKey);
                String simpleName = eventKey.contains(".") ? eventKey.substring(eventKey.lastIndexOf('.') + 1) : eventKey;
                
                UnifiedEventResult result = new UnifiedEventResult(
                        null, eventKey, simpleName, null, "Y", "SYNC",
                        null, null, null, null, null, null,
                        Collections.emptyList(), "미등록", listeners.size(), 0
                );
                resultMap.put(eventKey, result);
            }
        }
        
        return new ArrayList<>(resultMap.values());
    }

    @Override
    @Transactional(readOnly = true)
    public EventResult findByEventId(UUID eventId) {
        Event event = eventReadPort.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        return mapper.toEventResult(event);
    }

    @Override
    public EventResult editEvent(UUID eventId, EditEventCommand command) {
        Event event = eventReadPort.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        
        if (command.eventExpln() != null) {
            event.changeExpln(command.eventExpln());
        }
        if (command.useYn() != null) {
            if ("Y".equalsIgnoreCase(command.useYn())) {
                event.enable();
            } else {
                event.disable();
            }
        }
        if (command.dispatchType() != null) {
            event.changeDispatchType(Event.DispatchType.valueOf(command.dispatchType()));
        }
        
        Event saved = eventSavePort.save(event);
        return mapper.toEventResult(saved);
    }

    @Override
    public EventResult createEvent(CreateEventCommand command) {
        String eventKey = command.trimmedEventKey();
        if (eventReadPort.existsByEventKey(eventKey)) {
            throw new IllegalStateException("이미 등록된 이벤트입니다: " + eventKey);
        }
        
        String name = command.name();
        if (name == null || name.isBlank()) {
            name = eventKey.contains(".") ? eventKey.substring(eventKey.lastIndexOf('.') + 1) : eventKey;
        }
        
        Event event = Event.builder()
                .eventId(UUID.randomUUID())
                .eventKey(eventKey)
                .name(name)
                .eventExpln(command.eventExpln())
                .useYn("Y")
                .dispatchType(Event.DispatchType.SYNC)
                .build();
        
        Event saved = eventSavePort.save(event);
        return mapper.toEventResult(saved);
    }

    @Override
    public void deleteEvent(UUID eventId) {
        if (eventReadPort.findById(eventId).isEmpty()) {
            throw new EntityNotFoundException("Event not found: " + eventId);
        }
        eventDeletePort.deleteById(eventId);
    }

    @Override
    public void deleteEvents(List<UUID> eventIds) {
        eventDeletePort.deleteAllByIds(eventIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RuntimeListenerResult> listRuntimeListeners(String eventKeyOrId) {
        // UUID인지 확인
        try {
            UUID eventId = UUID.fromString(eventKeyOrId);
            Event event = eventReadPort.findById(eventId).orElse(null);
            if (event != null) {
                return runtimeScanner.getListenersByEventKey(event.getEventKey());
            }
        } catch (IllegalArgumentException ignored) {
            // UUID가 아니면 eventKey로 간주
        }
        return runtimeScanner.getListenersByEventKey(eventKeyOrId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<RuntimeListenerResult>> listAllRuntimeListenersGrouped() {
        return runtimeScanner.scanAllListeners();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScannedEventResult> previewScannedEvents(String sortBy, String order) {
        Map<String, List<RuntimeListenerResult>> map = runtimeScanner.scanAllListeners();
        
        List<ScannedEventResult> results = map.entrySet().stream()
                .map(e -> {
                    String eventKey = e.getKey();
                    String name = eventKey.contains(".") ? eventKey.substring(eventKey.lastIndexOf('.') + 1) : eventKey;
                    return new ScannedEventResult(eventKey, name, e.getValue().size());
                })
                .collect(Collectors.toList());
        
        // 정렬
        Comparator<ScannedEventResult> comparator = "count".equalsIgnoreCase(sortBy)
                ? Comparator.comparingInt(ScannedEventResult::listenerCount)
                : Comparator.comparing(ScannedEventResult::name);
        
        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }
        
        results.sort(comparator);
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DbListenerResult> listDbListeners(UUID eventId) {
        return listenerReadPort.findByEventId(eventId).stream()
                .map(mapper::toDbListenerResult)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DbListenerResult> listAllDbListeners() {
        return listenerReadPort.findAll().stream()
                .map(mapper::toDbListenerResult)
                .toList();
    }

    @Override
    public DbListenerResult editListener(UUID listenerId, EditListenerCommand command) {
        EventListener listener = listenerReadPort.findById(listenerId)
                .orElseThrow(() -> new EntityNotFoundException("Listener not found: " + listenerId));
        
        if (command.useYn() != null) {
            if ("Y".equalsIgnoreCase(command.useYn())) {
                listener.enable();
            } else {
                listener.disable();
            }
        }
        if (command.dsctn() != null) {
            listener.changeDescription(command.dsctn());
        }
        
        EventListener saved = listenerSavePort.save(listener);
        return mapper.toDbListenerResult(saved);
    }

    @Override
    public DbListenerResult createListener(CreateListenerCommand command) {
        Event event = eventReadPort.findByEventKey(command.eventKey())
                .orElseThrow(() -> new EntityNotFoundException("Event not found: " + command.eventKey()));
        
        // 중복 체크
        Optional<EventListener> existing = listenerReadPort.findByEventKeyAndBeanNameAndMethodNameAndBeanClassAndKind(
                command.eventKey(), command.beanName(), command.methodName(), command.beanClass(), command.kind());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 리스너입니다.");
        }
        
        EventListener listener = EventListener.builder()
                .listenerId(UUID.randomUUID())
                .eventId(event.getEventId())
                .eventKey(command.eventKey())
                .kind(EventListener.ListenerKind.valueOf(command.kind()))
                .beanName(command.beanName())
                .beanClass(command.beanClass())
                .methodName(command.methodName())
                .useYn(command.useYn() != null ? command.useYn() : "Y")
                .dsctn(command.dsctn())
                .build();
        
        EventListener saved = listenerSavePort.save(listener);
        return mapper.toDbListenerResult(saved);
    }

    @Override
    public List<ApplyUpdateResult> applyMissingListeners(List<UUID> eventIds) {
        List<ApplyUpdateResult> results = new ArrayList<>();
        
        for (UUID eventId : eventIds) {
            try {
                Event event = eventReadPort.findById(eventId).orElse(null);
                if (event == null) {
                    results.add(new ApplyUpdateResult(eventId, 0, 0, "이벤트를 찾을 수 없습니다."));
                    continue;
                }
                
                List<RuntimeListenerResult> runtimeListeners = runtimeScanner.getListenersByEventKey(event.getEventKey());
                List<EventListener> dbListeners = listenerReadPort.findByEventId(eventId);
                
                int created = 0;
                int skipped = 0;
                
                for (RuntimeListenerResult rt : runtimeListeners) {
                    boolean exists = dbListeners.stream().anyMatch(db ->
                            db.getBeanClass().equals(rt.beanClass()) &&
                            db.getMethodName().equals(rt.methodName()) &&
                            db.getKind().name().equals(rt.kind())
                    );
                    
                    if (exists) {
                        skipped++;
                    } else {
                        EventListener listener = EventListener.builder()
                                .listenerId(UUID.randomUUID())
                                .eventId(eventId)
                                .eventKey(event.getEventKey())
                                .kind(EventListener.ListenerKind.valueOf(rt.kind()))
                                .beanName(rt.beanName())
                                .beanClass(rt.beanClass())
                                .methodName(rt.methodName())
                                .useYn("Y")
                                .build();
                        listenerSavePort.save(listener);
                        created++;
                    }
                }
                
                results.add(new ApplyUpdateResult(eventId, created, skipped, null));
            } catch (Exception e) {
                results.add(new ApplyUpdateResult(eventId, 0, 0, e.getMessage()));
            }
        }
        
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnifiedListenerResult> listUnifiedListeners() {
        Map<String, List<RuntimeListenerResult>> runtimeMap = runtimeScanner.scanAllListeners();
        List<EventListener> dbListeners = listenerReadPort.findAll();
        List<Event> events = eventReadPort.findAll();
        Map<UUID, Event> eventMap = events.stream().collect(Collectors.toMap(Event::getEventId, e -> e));
        
        List<UnifiedListenerResult> results = new ArrayList<>();
        
        // DB 리스너 추가
        for (EventListener listener : dbListeners) {
            Event event = eventMap.get(listener.getEventId());
            String eventName = event != null ? event.getName() : null;
            
            // 런타임에 존재하는지 확인
            List<RuntimeListenerResult> runtimeList = runtimeMap.getOrDefault(listener.getEventKey(), Collections.emptyList());
            boolean existsInRuntime = runtimeList.stream().anyMatch(rt ->
                    rt.beanClass().equals(listener.getBeanClass()) &&
                    rt.methodName().equals(listener.getMethodName())
            );
            
            String registerType = existsInRuntime ? "등록" : "삭제";
            
            results.add(new UnifiedListenerResult(
                    listener.getListenerId(),
                    listener.getEventId(),
                    listener.getEventKey(),
                    eventName,
                    listener.getKind().name(),
                    listener.getBeanName(),
                    listener.getBeanClass(),
                    listener.getMethodName(),
                    listener.getUseYn(),
                    registerType,
                    listener.getDsctn()
            ));
        }
        
        // 런타임에만 있는 리스너 추가
        for (Map.Entry<String, List<RuntimeListenerResult>> entry : runtimeMap.entrySet()) {
            String eventKey = entry.getKey();
            Event event = events.stream().filter(e -> e.getEventKey().equals(eventKey)).findFirst().orElse(null);
            
            for (RuntimeListenerResult rt : entry.getValue()) {
                boolean existsInDb = dbListeners.stream().anyMatch(db ->
                        db.getEventKey().equals(eventKey) &&
                        db.getBeanClass().equals(rt.beanClass()) &&
                        db.getMethodName().equals(rt.methodName())
                );
                
                if (!existsInDb) {
                    String simpleName = eventKey.contains(".") ? eventKey.substring(eventKey.lastIndexOf('.') + 1) : eventKey;
                    results.add(new UnifiedListenerResult(
                            null,
                            event != null ? event.getEventId() : null,
                            eventKey,
                            event != null ? event.getName() : simpleName,
                            rt.kind(),
                            rt.beanName(),
                            rt.beanClass(),
                            rt.methodName(),
                            "Y",
                            "미등록",
                            rt.description()
                    ));
                }
            }
        }
        
        return results;
    }

    @Override
    public long migrateListenerKindToHandler(boolean dryRun) {
        // 간단한 구현: HANDLER로 마이그레이션할 대상 카운트
        List<EventListener> all = listenerReadPort.findAll();
        long count = all.stream()
                .filter(l -> l.getKind() != EventListener.ListenerKind.HANDLER)
                .count();
        
        if (!dryRun) {
            for (EventListener listener : all) {
                if (listener.getKind() != EventListener.ListenerKind.HANDLER) {
                    // 실제 마이그레이션 로직은 소비자 프로젝트에서 구현
                    log.info("Listener {} would be migrated to HANDLER", listener.getListenerId());
                }
            }
        }
        
        return count;
    }
}
