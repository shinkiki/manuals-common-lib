package kr.co.manuals.common.event.infrastructure.adapter;

import kr.co.manuals.common.event.domain.Event;
import kr.co.manuals.common.event.domain.port.EventDeletePort;
import kr.co.manuals.common.event.domain.port.EventReadPort;
import kr.co.manuals.common.event.domain.port.EventRepository;
import kr.co.manuals.common.event.domain.port.EventSavePort;
import kr.co.manuals.common.event.infrastructure.entity.EventEntity;
import kr.co.manuals.common.event.infrastructure.mapper.EventInfraMapper;
import kr.co.manuals.common.event.infrastructure.repository.EventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepository, EventReadPort, EventSavePort, EventDeletePort {

    private final EventJpaRepository repository;
    private final EventInfraMapper mapper;

    @Override
    public Optional<Event> findById(UUID id) {
        return repository.findById(id).map(mapper::toEvent);
    }

    @Override
    public Optional<Event> findByEventKey(String eventKey) {
        return repository.findByEventKey(eventKey).map(mapper::toEvent);
    }

    @Override
    public boolean existsByEventKey(String eventKey) {
        return repository.existsByEventKey(eventKey);
    }

    @Override
    public List<Event> findAll() {
        return repository.findAll().stream().map(mapper::toEvent).toList();
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = mapper.toEventEntity(event);
        EventEntity saved = repository.save(entity);
        return mapper.toEvent(saved);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAllByIds(List<UUID> ids) {
        repository.deleteAllByIdInBatch(ids);
    }
}
