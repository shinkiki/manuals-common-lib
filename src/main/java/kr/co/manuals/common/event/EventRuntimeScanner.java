package kr.co.manuals.common.event;

import java.util.Set;

/**
 * 런타임에 이벤트를 스캔하는 인터페이스
 * <p>
 * 사용하는 프로젝트에서 이 인터페이스를 구현하여 Bean으로 등록하면
 * 애플리케이션 컨텍스트에서 이벤트 클래스를 스캔합니다.
 * </p>
 *
 * <pre>{@code
 * @Component
 * public class MyEventRuntimeScanner implements EventRuntimeScanner {
 *     @Override
 *     public Set<Class<?>> scanEventClasses() {
 *         return Set.of(
 *             UserCreatedEvent.class,
 *             OrderCompletedEvent.class
 *         );
 *     }
 * }
 * }</pre>
 */
@FunctionalInterface
public interface EventRuntimeScanner {
    
    /**
     * 이벤트 클래스들을 스캔하여 반환
     *
     * @return 이벤트 클래스 Set
     */
    Set<Class<?>> scanEventClasses();
}
