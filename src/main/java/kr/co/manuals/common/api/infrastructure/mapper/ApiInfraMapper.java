package kr.co.manuals.common.api.infrastructure.mapper;

import kr.co.manuals.common.api.domain.Api;
import kr.co.manuals.common.api.infrastructure.entity.ApiEntity;
import kr.co.manuals.common.settings.config.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class)
public interface ApiInfraMapper {
    Api toApi(ApiEntity entity);
    ApiEntity toApiEntity(Api api);
}
