package kr.co.manuals.common.api.presentation.mapper;

import kr.co.manuals.common.api.application.dto.ApiDiscoveryResult;
import kr.co.manuals.common.api.application.dto.ApiResult;
import kr.co.manuals.common.api.application.dto.EditApiCommand;
import kr.co.manuals.common.api.application.dto.SaveApiCommand;
import kr.co.manuals.common.api.presentation.dto.*;
import kr.co.manuals.common.settings.config.CommonMapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = CommonMapperConfig.class)
public interface ApiPresentationMapper {
    SaveApiCommand toSaveCommand(SaveApiRequest request);
    EditApiCommand toEditCommand(EditApiRequest request);
    SaveApiResponse toSaveResponse(ApiResult result);
    EditApiResponse toEditResponse(ApiResult result);
    ReadApiResponse toReadResponse(ApiResult result);
    ListApiResponse toListResponse(ApiDiscoveryResult result);
    List<ListApiResponse> toListResponses(List<ApiDiscoveryResult> results);
}
