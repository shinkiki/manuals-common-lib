package kr.co.manuals.common.api.domain.port;

import kr.co.manuals.common.api.domain.Api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiReadPort {
    Optional<Api> findByApiId(UUID apiId);
    List<Api> findAll();
}
