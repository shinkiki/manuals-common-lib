package kr.co.manuals.common.event.application.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 이벤트 발행을 위한 공통 퍼블리셔
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommonEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(Object event) {
        if (event == null) {
            log.warn("null 이벤트는 발행할 수 없습니다.");
            return;
        }
        log.debug("이벤트 발행: {}", event.getClass().getName());
        applicationEventPublisher.publishEvent(event);
    }
}
