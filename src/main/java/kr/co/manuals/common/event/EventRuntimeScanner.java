package kr.co.manuals.common.event;

import kr.co.manuals.common.event.application.dto.RuntimeListenerResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 런타임 이벤트 스캐너 인터페이스
 * <p>
 * 프로젝트에서 이 인터페이스를 구현하여 이벤트 클래스와 리스너를 스캔합니다.
 * </p>
 *
 * <h3>사용 예시:</h3>
 * <pre>{@code
 * @Component
 * public class MyEventScanner implements EventRuntimeScanner {
 *     @Override
 *     public Set<Class<?>> scanEventClasses() {
 *         return new Reflections("com.example.events")
 *             .getTypesAnnotatedWith(ManualsEvent.class);
 *     }
 *
 *     @Override
 *     public Map<String, List<RuntimeListenerResult>> scanAllListeners() {
 *         // 모든 리스너 스캔 구현
 *     }
 *
 *     @Override
 *     public List<RuntimeListenerResult> getListenersByEventKey(String eventKey) {
 *         return scanAllListeners().getOrDefault(eventKey, List.of());
 *     }
 * }
 * }</pre>
 */
public interface EventRuntimeScanner {

    /**
     * 이벤트 클래스를 스캔합니다.
     *
     * @return 스캔된 이벤트 클래스 집합
     */
    default Set<Class<?>> scanEventClasses() {
        return Set.of();
    }

    /**
     * 모든 이벤트 리스너를 스캔합니다.
     *
     * @return 이벤트 키별 리스너 목록 맵
     */
    default Map<String, List<RuntimeListenerResult>> scanAllListeners() {
        return Map.of();
    }

    /**
     * 특정 이벤트 키에 대한 리스너를 조회합니다.
     *
     * @param eventKey 이벤트 키
     * @return 해당 이벤트에 등록된 리스너 목록
     */
    default List<RuntimeListenerResult> getListenersByEventKey(String eventKey) {
        return List.of();
    }
}
