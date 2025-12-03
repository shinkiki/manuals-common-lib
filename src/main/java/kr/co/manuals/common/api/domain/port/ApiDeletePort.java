package kr.co.manuals.common.api.domain.port;

import java.util.List;
import java.util.UUID;

public interface ApiDeletePort {
    void deleteAll(List<UUID> apiIds);
}
