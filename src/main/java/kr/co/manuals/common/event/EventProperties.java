package kr.co.manuals.common.event;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 이벤트 시스템 설정 프로퍼티
 */
@ConfigurationProperties(prefix = "manuals.common.event")
public class EventProperties {
    
    /**
     * 이벤트 시스템 활성화 여부
     */
    private boolean enabled = true;
    
    /**
     * API 기본 경로
     */
    private String apiPath = "/v1.0/system/events";
    
    /**
     * 애플리케이션 시작 시 자동 동기화 여부
     */
    private boolean autoSync = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public boolean isAutoSync() {
        return autoSync;
    }

    public void setAutoSync(boolean autoSync) {
        this.autoSync = autoSync;
    }
}
