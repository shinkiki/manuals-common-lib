package kr.co.manuals.common.event;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 이벤트 시스템 자동 구성
 * <p>
 * 이 라이브러리를 의존성으로 추가하면 이벤트 관리 기능이 자동으로 활성화됩니다.
 * </p>
 *
 * <h3>설정 프로퍼티:</h3>
 * <ul>
 *   <li>{@code manuals.common.event.enabled=true} - 이벤트 시스템 활성화 (기본값: true)</li>
 *   <li>{@code manuals.common.event.api-path=/v1.0/system/events} - API 경로 (기본값)</li>
 *   <li>{@code manuals.common.event.auto-sync=true} - 애플리케이션 시작 시 자동 동기화</li>
 * </ul>
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "manuals.common.event", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(EventProperties.class)
@EnableJpaRepositories(basePackages = "kr.co.manuals.common.event.infrastructure.repository")
@EntityScan(basePackages = "kr.co.manuals.common.event.infrastructure.entity")
@ComponentScan(basePackages = {
        "kr.co.manuals.common.event.application",
        "kr.co.manuals.common.event.presentation",
        "kr.co.manuals.common.event.infrastructure"
})
public class EventAutoConfiguration {

    /**
     * 기본 EventRuntimeScanner (No-op 구현)
     * <p>
     * 프로젝트별로 커스텀 스캐너를 제공하지 않으면 빈 결과를 반환합니다.
     * </p>
     */
    @Bean
    @ConditionalOnMissingBean(EventRuntimeScanner.class)
    public EventRuntimeScanner defaultEventRuntimeScanner() {
        return new EventRuntimeScanner() {};
    }
}
