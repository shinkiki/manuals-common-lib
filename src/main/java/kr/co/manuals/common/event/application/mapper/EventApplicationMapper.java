package kr.co.manuals.common.event.application.mapper;

import kr.co.manuals.common.event.application.dto.*;
import kr.co.manuals.common.event.domain.Event;
import kr.co.manuals.common.event.domain.EventListener;
import kr.co.manuals.common.settings.config.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface EventApplicationMapper {

    @Mapping(target = "registerType", ignore = true)
    @Mapping(target = "runtimeListenerCount", ignore = true)
    @Mapping(target = "dbListenerCount", ignore = true)
    EventResult toEventResult(Event event);

    @Mapping(target = "registerType", ignore = true)
    @Mapping(target = "runtimeListenerCount", ignore = true)
    @Mapping(target = "dbListenerCount", ignore = true)
    UnifiedEventResult toUnifiedEventResult(Event event);

    @Mapping(target = "kind", source = "kind")
    DbListenerResult toDbListenerResult(EventListener listener);

    default String mapDispatchType(Event.DispatchType type) {
        return type == null ? null : type.name();
    }

    default String mapListenerKind(EventListener.ListenerKind kind) {
        return kind == null ? null : kind.name();
    }
}
