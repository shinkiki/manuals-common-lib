package kr.co.manuals.common.api.infrastructure.adapter;

import kr.co.manuals.common.api.domain.port.ApiDeletePort;
import kr.co.manuals.common.api.infrastructure.repository.ApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApiDeleteAdapter implements ApiDeletePort {
    private final ApiRepository repository;

    @Override
    public void deleteAll(List<UUID> apiIds) {
        if (apiIds == null || apiIds.isEmpty()) return;
        repository.deleteAllById(apiIds);
    }
}
