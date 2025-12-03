package kr.co.manuals.common.api.infrastructure.adapter;

import kr.co.manuals.common.api.domain.Api;
import kr.co.manuals.common.api.domain.port.ApiUpdatePort;
import kr.co.manuals.common.api.infrastructure.entity.ApiEntity;
import kr.co.manuals.common.api.infrastructure.mapper.ApiInfraMapper;
import kr.co.manuals.common.api.infrastructure.repository.ApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiUpdateAdapter implements ApiUpdatePort {
    private final ApiRepository repository;
    private final ApiInfraMapper mapper;

    @Override
    public Api update(Api api) {
        ApiEntity saved = repository.save(mapper.toApiEntity(api));
        return mapper.toApi(saved);
    }
}
