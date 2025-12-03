package kr.co.manuals.common.api.application.mapper;

import kr.co.manuals.common.api.application.dto.ApiResult;
import kr.co.manuals.common.api.application.dto.EditApiCommand;
import kr.co.manuals.common.api.application.dto.SaveApiCommand;
import kr.co.manuals.common.api.domain.Api;
import kr.co.manuals.common.settings.config.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapperConfig.class)
public interface ApiApplicationMapper {
    Api toDomain(SaveApiCommand command);
    ApiResult toResult(Api api);
    Api applyEdit(@MappingTarget Api api, EditApiCommand command);
}
