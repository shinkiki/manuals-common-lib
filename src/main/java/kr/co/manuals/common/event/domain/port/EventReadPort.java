package kr.co.manuals.common.event.domain.port;

import kr.co.manuals.common.event.domain.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventReadPort {
    Optional<Event> findById(UUID id);
    Optional<Event> findByEventKey(String eventKey);
    boolean existsByEventKey(String eventKey);
    List<Event> findAll();
}
