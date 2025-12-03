package kr.co.manuals.common.event.domain.port;

import kr.co.manuals.common.event.domain.EventListener;

public interface EventListenerSavePort {
    EventListener save(EventListener listener);
}
