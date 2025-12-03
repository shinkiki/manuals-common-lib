package kr.co.manuals.common.event.domain.port;

import kr.co.manuals.common.event.domain.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 이벤트 레포지토리 포트
 */
public interface EventRepository {
    
    /**
     * 이벤트 저장
     */
    Event save(Event event);
    
    /**
     * ID로 이벤트 조회
     */
    Optional<Event> findById(UUID eventId);
    
    /**
     * 이벤트 키로 조회
     */
    Optional<Event> findByEventKey(String eventKey);
    
    /**
     * 모든 이벤트 조회
     */
    List<Event> findAll();
    
    /**
     * 이벤트 삭제
     */
    void deleteById(UUID eventId);
    
    /**
     * 이벤트 키 존재 여부
     */
    boolean existsByEventKey(String eventKey);
}
