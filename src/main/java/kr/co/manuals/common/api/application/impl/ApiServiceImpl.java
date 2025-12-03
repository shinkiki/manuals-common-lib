package kr.co.manuals.common.api.application.impl;

import kr.co.manuals.common.api.application.ApiService;
import kr.co.manuals.common.api.application.ApiUseCase;
import kr.co.manuals.common.api.application.dto.ApiDiscoveryResult;
import kr.co.manuals.common.api.application.dto.ApiResult;
import kr.co.manuals.common.api.application.dto.EditApiCommand;
import kr.co.manuals.common.api.application.dto.SaveApiCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiServiceImpl implements ApiService {
    private final ApiUseCase apiUseCase;

    @Override
    public ApiResult saveApi(SaveApiCommand command) {
        return apiUseCase.saveApi(command);
    }

    @Override
    public ApiResult editApi(UUID apiId, EditApiCommand command) {
        return apiUseCase.editApi(apiId, command);
    }

    @Override
    public void deleteApi(UUID apiId) {
        apiUseCase.deleteApi(apiId);
    }

    @Override
    public void deleteApis(List<UUID> apiIds) {
        apiUseCase.deleteApis(apiIds);
    }

    @Override
    public ApiResult findByApiId(UUID apiId) {
        return apiUseCase.findByApiId(apiId);
    }

    @Override
    public List<ApiResult> findAll() {
        return apiUseCase.findAll();
    }

    @Override
    public List<ApiDiscoveryResult> discoverAllWithUnregistered(Boolean changed, Boolean registered) {
        return apiUseCase.discoverAllWithUnregistered(changed, registered);
    }
}
