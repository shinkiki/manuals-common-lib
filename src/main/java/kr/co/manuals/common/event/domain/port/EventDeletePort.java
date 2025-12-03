package kr.co.manuals.common.event.domain.port;

import java.util.List;
import java.util.UUID;

public interface EventDeletePort {
    void deleteById(UUID id);
    void deleteAllByIds(List<UUID> ids);
}
