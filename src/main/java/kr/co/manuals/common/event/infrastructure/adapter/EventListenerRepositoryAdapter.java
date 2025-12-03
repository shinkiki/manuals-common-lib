package kr.co.manuals.common.event.infrastructure.adapter;

import kr.co.manuals.common.event.domain.EventListener;
import kr.co.manuals.common.event.domain.port.EventListenerReadPort;
import kr.co.manuals.common.event.domain.port.EventListenerSavePort;
import kr.co.manuals.common.event.infrastructure.entity.EventEntity;
import kr.co.manuals.common.event.infrastructure.entity.EventListenerEntity;
import kr.co.manuals.common.event.infrastructure.mapper.EventInfraMapper;
import kr.co.manuals.common.event.infrastructure.repository.EventJpaRepository;
import kr.co.manuals.common.event.infrastructure.repository.EventListenerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventListenerRepositoryAdapter implements EventListenerReadPort, EventListenerSavePort {

    private final EventListenerJpaRepository repository;
    private final EventJpaRepository eventRepository;
    private final EventInfraMapper mapper;

    @Override
    public Optional<EventListener> findById(UUID id) {
        return repository.findById(id).map(mapper::toEventListener);
    }

    @Override
    public List<EventListener> findByEventId(UUID eventId) {
        return repository.findByEvent_EventIdOrderByBeanClassAscMethodNameAsc(eventId)
                .stream().map(mapper::toEventListener).toList();
    }

    @Override
    public List<EventListener> findByEventKey(String eventKey) {
        return repository.findByEventKeyOrderByBeanClassAscMethodNameAsc(eventKey)
                .stream().map(mapper::toEventListener).toList();
    }

    @Override
    public List<EventListener> findAll() {
        return repository.findAll().stream().map(mapper::toEventListener).toList();
    }

    @Override
    public Optional<EventListener> findByEventKeyAndBeanNameAndMethodNameAndBeanClassAndKind(
            String eventKey, String beanName, String methodName, String beanClass, String kind) {
        return repository.findByEventKeyAndBeanNameAndMethodNameAndBeanClassAndKind(
                eventKey, beanName, methodName, beanClass, kind
        ).map(mapper::toEventListener);
    }

    @Override
    public List<EventListener> findByEventIds(List<UUID> eventIds) {
        return repository.findByEvent_EventIdIn(eventIds)
                .stream().map(mapper::toEventListener).toList();
    }

    @Override
    public EventListener save(EventListener listener) {
        EventListenerEntity entity = mapper.toEventListenerEntity(listener);
        
        // event 연관관계 설정
        if (listener.getEventId() != null) {
            EventEntity eventEntity = eventRepository.findById(listener.getEventId()).orElse(null);
            entity.setEvent(eventEntity);
        }
        
        EventListenerEntity saved = repository.save(entity);
        return mapper.toEventListener(saved);
    }
}
