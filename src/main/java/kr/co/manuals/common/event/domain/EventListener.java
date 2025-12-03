package kr.co.manuals.common.event.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class EventListener {

    public enum ListenerKind {
        ANNOTATION,
        INTERFACE,
        HANDLER
    }

    private UUID listenerId;
    private UUID eventId;
    private String eventKey;
    private ListenerKind kind;
    private String beanName;
    private String beanClass;
    private String methodName;
    private String useYn;
    private String dsctn;

    // 감사 필드
    private UUID rgtrId;
    private String rgtr;
    private LocalDateTime regDt;
    private UUID mdfrId;
    private String mdfr;
    private LocalDateTime mdfcnDt;

    public void enable() {
        this.useYn = "Y";
    }

    public void disable() {
        this.useYn = "N";
    }

    public void changeDescription(String description) {
        this.dsctn = description;
    }
}
