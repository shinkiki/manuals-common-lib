package kr.co.manuals.common.event.infrastructure.repository;

import kr.co.manuals.common.event.infrastructure.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventJpaRepository extends JpaRepository<EventEntity, UUID> {
    Optional<EventEntity> findByEventKey(String eventKey);
    boolean existsByEventKey(String eventKey);
}
