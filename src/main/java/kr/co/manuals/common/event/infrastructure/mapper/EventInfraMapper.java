package kr.co.manuals.common.event.infrastructure.mapper;

import kr.co.manuals.common.event.domain.Event;
import kr.co.manuals.common.event.domain.EventListener;
import kr.co.manuals.common.event.infrastructure.entity.EventEntity;
import kr.co.manuals.common.event.infrastructure.entity.EventListenerEntity;
import kr.co.manuals.common.settings.config.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface EventInfraMapper {

    Event toEvent(EventEntity entity);

    EventEntity toEventEntity(Event event);

    @Mapping(target = "eventId", source = "event.eventId")
    EventListener toEventListener(EventListenerEntity entity);

    @Mapping(target = "event", ignore = true)
    EventListenerEntity toEventListenerEntity(EventListener listener);

    // enum mapping helpers
    default Event.DispatchType toDomainDispatchType(EventEntity.DispatchType dt) {
        if (dt == null) return null;
        return switch (dt) {
            case SYNC -> Event.DispatchType.SYNC;
            case ASYNC -> Event.DispatchType.ASYNC;
        };
    }

    default EventEntity.DispatchType toEntityDispatchType(Event.DispatchType dt) {
        if (dt == null) return null;
        return switch (dt) {
            case SYNC -> EventEntity.DispatchType.SYNC;
            case ASYNC -> EventEntity.DispatchType.ASYNC;
        };
    }

    default EventListener.ListenerKind toDomainListenerKind(String kind) {
        if (kind == null) return null;
        return EventListener.ListenerKind.valueOf(kind);
    }

    default String toEntityListenerKind(EventListener.ListenerKind kind) {
        if (kind == null) return null;
        return kind.name();
    }
}
