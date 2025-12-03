package kr.co.manuals.common.api.application;

import kr.co.manuals.common.api.application.dto.ApiDiscoveryResult;
import kr.co.manuals.common.api.application.dto.ApiResult;
import kr.co.manuals.common.api.application.dto.EditApiCommand;
import kr.co.manuals.common.api.application.dto.SaveApiCommand;

import java.util.List;
import java.util.UUID;

public interface ApiUseCase {
    ApiResult saveApi(SaveApiCommand command);
    ApiResult editApi(UUID apiId, EditApiCommand command);
    void deleteApi(UUID apiId);
    void deleteApis(List<UUID> apiIds);
    ApiResult findByApiId(UUID apiId);
    List<ApiResult> findAll();
    List<ApiDiscoveryResult> discoverAllWithUnregistered(Boolean changed, Boolean registered);
}
