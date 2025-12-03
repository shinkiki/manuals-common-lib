package kr.co.manuals.common.event.infrastructure.repository;

import kr.co.manuals.common.event.infrastructure.entity.EventListenerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventListenerJpaRepository extends JpaRepository<EventListenerEntity, UUID> {
    List<EventListenerEntity> findByEventKeyOrderByBeanClassAscMethodNameAsc(String eventKey);
    Optional<EventListenerEntity> findByEventKeyAndBeanNameAndMethodNameAndBeanClassAndKind(
            String eventKey, String beanName, String methodName, String beanClass, String kind);
    List<EventListenerEntity> findByEvent_EventIdOrderByBeanClassAscMethodNameAsc(UUID eventId);
    List<EventListenerEntity> findByEvent_EventIdIn(Collection<UUID> eventIds);
}
