package kr.co.manuals.common.event.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Event {

    public enum DispatchType {
        SYNC,
        ASYNC
    }

    private UUID eventId;
    private String eventKey;
    private String name;
    private String eventExpln;
    private List<UUID> listenerIdList;
    private String useYn;
    private DispatchType dispatchType;

    // 감사 필드
    private UUID rgtrId;
    private String rgtr;
    private LocalDateTime regDt;
    private UUID mdfrId;
    private String mdfr;
    private LocalDateTime mdfcnDt;

    public void changeExpln(String expln) {
        this.eventExpln = expln;
    }

    public void enable() {
        this.useYn = "Y";
    }

    public void disable() {
        this.useYn = "N";
    }

    public void changeDispatchType(DispatchType type) {
        if (type == null) throw new IllegalArgumentException("dispatchType is required");
        this.dispatchType = type;
    }
}
