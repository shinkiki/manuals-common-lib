package kr.co.manuals.common.api.application.impl;

import kr.co.manuals.common.api.application.ApiUseCase;
import kr.co.manuals.common.api.application.dto.*;
import kr.co.manuals.common.api.application.mapper.ApiApplicationMapper;
import kr.co.manuals.common.api.application.support.SwaggerSuggester;
import kr.co.manuals.common.api.domain.Api;
import kr.co.manuals.common.api.domain.port.ApiDeletePort;
import kr.co.manuals.common.api.domain.port.ApiReadPort;
import kr.co.manuals.common.api.domain.port.ApiSavePort;
import kr.co.manuals.common.api.domain.port.ApiUpdatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiUseCaseImpl implements ApiUseCase {
    private final ApiReadPort readPort;
    private final ApiSavePort savePort;
    private final ApiUpdatePort updatePort;
    private final ApiDeletePort deletePort;
    private final ApiApplicationMapper mapper;
    private final SwaggerSuggester swaggerSuggester;

    @Override
    public ApiResult saveApi(SaveApiCommand command) {
        Api api = mapper.toDomain(command);
        api.setApiId(UUID.randomUUID());
        complementFromSwagger(api);
        Api saved = savePort.save(api);
        return mapper.toResult(saved);
    }

    @Override
    public ApiResult editApi(UUID apiId, EditApiCommand command) {
        Api api = readPort.findByApiId(apiId)
                .orElseThrow(() -> new IllegalArgumentException("API not found: " + apiId));
        mapper.applyEdit(api, command);
        complementFromSwagger(api);
        Api updated = updatePort.update(api);
        return mapper.toResult(updated);
    }

    @Override
    public void deleteApi(UUID apiId) {
        deletePort.deleteAll(List.of(apiId));
    }

    @Override
    public void deleteApis(List<UUID> apiIds) {
        deletePort.deleteAll(apiIds);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResult findByApiId(UUID apiId) {
        Api api = readPort.findByApiId(apiId)
                .orElseThrow(() -> new IllegalArgumentException("API not found: " + apiId));
        return mapper.toResult(api);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiResult> findAll() {
        return readPort.findAll().stream()
                .map(mapper::toResult)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApiDiscoveryResult> discoverAllWithUnregistered(Boolean changed, Boolean registered) {
        List<Api> dbApis = readPort.findAll();
        Map<String, Api> dbMap = dbApis.stream()
                .collect(Collectors.toMap(
                        a -> buildKey(a.getMethodType(), a.getApiUrl()),
                        a -> a,
                        (a, b) -> a
                ));
        
        List<SwaggerSuggester.Endpoint> endpoints = swaggerSuggester.endpoints();
        Set<String> swaggerKeys = endpoints.stream()
                .map(ep -> buildKey(ep.method(), ep.path()))
                .collect(Collectors.toSet());
        
        List<ApiDiscoveryResult> results = new ArrayList<>();
        
        // DB에 등록된 API
        for (Api api : dbApis) {
            String key = buildKey(api.getMethodType(), api.getApiUrl());
            boolean existsInSwagger = swaggerKeys.contains(key);
            
            SwaggerSuggester.Examples ex = swaggerSuggester.suggest(api.getMethodType(), api.getApiUrl());
            boolean isChanged = checkChanged(api, ex);
            
            String registerType;
            if (!existsInSwagger && !swaggerKeys.isEmpty()) {
                registerType = "삭제";
            } else if (isChanged) {
                registerType = "변경";
            } else {
                registerType = "등록";
            }
            
            results.add(new ApiDiscoveryResult(
                    api.getApiId(),
                    api.getMethodType(),
                    api.getApiNm(),
                    api.getApiDsctn(),
                    api.getApiUrl(),
                    true,
                    isChanged,
                    registerType
            ));
        }
        
        // Swagger에만 있는 미등록 API
        for (SwaggerSuggester.Endpoint ep : endpoints) {
            String key = buildKey(ep.method(), ep.path());
            if (!dbMap.containsKey(key)) {
                results.add(new ApiDiscoveryResult(
                        null,
                        ep.method(),
                        ep.apiNm(),
                        ep.apiDsctn(),
                        ep.path(),
                        false,
                        false,
                        "미등록"
                ));
            }
        }
        
        // Filter by changed/registered
        if (changed != null) {
            results = results.stream()
                    .filter(r -> r.changed() == changed)
                    .collect(Collectors.toList());
        }
        if (registered != null) {
            results = results.stream()
                    .filter(r -> r.registered() == registered)
                    .collect(Collectors.toList());
        }
        
        return results;
    }

    private void complementFromSwagger(Api api) {
        SwaggerSuggester.Examples ex = swaggerSuggester.suggest(api.getMethodType(), api.getApiUrl());
        if (isBlank(api.getHeader()) && ex.getHeader() != null) api.setHeader(ex.getHeader());
        if (isBlank(api.getBody()) && ex.getBody() != null) api.setBody(ex.getBody());
        if (isBlank(api.getResponse()) && ex.getResponse() != null) api.setResponse(ex.getResponse());
        if (isBlank(api.getApiNm()) && ex.getApiNm() != null) api.setApiNm(ex.getApiNm());
        if (isBlank(api.getApiDsctn()) && ex.getApiDsctn() != null) api.setApiDsctn(ex.getApiDsctn());
    }

    private boolean checkChanged(Api api, SwaggerSuggester.Examples ex) {
        if (ex.getBody() != null && !ex.getBody().equals(api.getBody())) return true;
        if (ex.getResponse() != null && !ex.getResponse().equals(api.getResponse())) return true;
        return false;
    }

    private String buildKey(String method, String path) {
        return ((method == null ? "" : method).toUpperCase(Locale.ROOT) + ":" + normalizePath(path)).toUpperCase(Locale.ROOT);
    }

    private String normalizePath(String p) {
        if (p == null) return "";
        String r = p.trim().replaceAll("//+", "/");
        if (r.endsWith("/") && r.length() > 1) r = r.substring(0, r.length() - 1);
        return r;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
