package kr.co.manuals.common.event.domain.port;

import kr.co.manuals.common.event.domain.EventListener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventListenerReadPort {
    Optional<EventListener> findById(UUID id);
    List<EventListener> findByEventId(UUID eventId);
    List<EventListener> findByEventKey(String eventKey);
    List<EventListener> findAll();
    Optional<EventListener> findByEventKeyAndBeanNameAndMethodNameAndBeanClassAndKind(
            String eventKey, String beanName, String methodName, String beanClass, String kind);
    List<EventListener> findByEventIds(List<UUID> eventIds);
}
