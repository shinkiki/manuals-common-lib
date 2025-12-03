package kr.co.manuals.common.event.domain.port;

import kr.co.manuals.common.event.domain.Event;

public interface EventSavePort {
    Event save(Event event);
}
