package kr.co.manuals.common.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 공통 이벤트 퍼블리셔
 * <p>
 * Spring ApplicationEventPublisher를 래핑하여 이벤트를 발행합니다.
 * </p>
 *
 * <pre>{@code
 * @Service
 * @RequiredArgsConstructor
 * public class UserService {
 *     private final CommonEventPublisher eventPublisher;
 *     
 *     public void createUser(User user) {
 *         // 사용자 생성 로직
 *         eventPublisher.publish(new UserCreatedEvent(user));
 *     }
 * }
 * }</pre>
 */
@Component
public class CommonEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public CommonEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 이벤트 발행
     *
     * @param event 발행할 이벤트 객체
     */
    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
