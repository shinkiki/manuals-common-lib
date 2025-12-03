package kr.co.manuals.common.event;

/**
 * 도메인 이벤트 핸들러 인터페이스
 * <p>
 * 특정 이벤트 타입을 처리하는 핸들러를 구현합니다.
 * </p>
 *
 * <pre>{@code
 * @Component
 * public class UserCreatedEventHandler implements DomainEventHandler<UserCreatedEvent> {
 *     @Override
 *     public Class<UserCreatedEvent> getEventType() {
 *         return UserCreatedEvent.class;
 *     }
 *     
 *     @Override
 *     public void handle(UserCreatedEvent event) {
 *         // 이벤트 처리 로직
 *     }
 * }
 * }</pre>
 *
 * @param <T> 이벤트 타입
 */
public interface DomainEventHandler<T> {
    
    /**
     * 이 핸들러가 처리하는 이벤트 타입을 반환
     *
     * @return 이벤트 클래스
     */
    Class<T> getEventType();
    
    /**
     * 이벤트 처리
     *
     * @param event 이벤트 객체
     */
    void handle(T event);
}
