package kr.co.manuals.common.api.infrastructure.mapper;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import kr.co.manuals.common.api.domain.Api;
import kr.co.manuals.common.api.infrastructure.entity.ApiEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T14:24:01+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21.0.9 (Homebrew)"
)
@Component
public class ApiInfraMapperImpl implements ApiInfraMapper {

    @Override
    public Api toApi(ApiEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UUID apiId = null;
        String methodType = null;
        String apiNm = null;
        String apiDsctn = null;
        String apiUrl = null;
        String header = null;
        String body = null;
        String response = null;
        UUID rgtrId = null;
        String rgtr = null;
        LocalDateTime regDt = null;
        UUID mdfrId = null;
        String mdfr = null;
        LocalDateTime mdfcnDt = null;

        apiId = entity.getApiId();
        methodType = entity.getMethodType();
        apiNm = entity.getApiNm();
        apiDsctn = entity.getApiDsctn();
        apiUrl = entity.getApiUrl();
        header = entity.getHeader();
        body = entity.getBody();
        response = entity.getResponse();
        rgtrId = entity.getRgtrId();
        rgtr = entity.getRgtr();
        regDt = entity.getRegDt();
        mdfrId = entity.getMdfrId();
        mdfr = entity.getMdfr();
        mdfcnDt = entity.getMdfcnDt();

        Api api = new Api( apiId, methodType, apiNm, apiDsctn, apiUrl, header, body, response, rgtrId, rgtr, regDt, mdfrId, mdfr, mdfcnDt );

        return api;
    }

    @Override
    public ApiEntity toApiEntity(Api api) {
        if ( api == null ) {
            return null;
        }

        UUID apiId = null;
        String methodType = null;
        String apiNm = null;
        String apiDsctn = null;
        String apiUrl = null;
        String header = null;
        String body = null;
        String response = null;
        UUID rgtrId = null;
        String rgtr = null;
        LocalDateTime regDt = null;
        UUID mdfrId = null;
        String mdfr = null;
        LocalDateTime mdfcnDt = null;

        apiId = api.getApiId();
        methodType = api.getMethodType();
        apiNm = api.getApiNm();
        apiDsctn = api.getApiDsctn();
        apiUrl = api.getApiUrl();
        header = api.getHeader();
        body = api.getBody();
        response = api.getResponse();
        rgtrId = api.getRgtrId();
        rgtr = api.getRgtr();
        regDt = api.getRegDt();
        mdfrId = api.getMdfrId();
        mdfr = api.getMdfr();
        mdfcnDt = api.getMdfcnDt();

        ApiEntity apiEntity = new ApiEntity( apiId, methodType, apiNm, apiDsctn, apiUrl, header, body, response, rgtrId, rgtr, regDt, mdfrId, mdfr, mdfcnDt );

        return apiEntity;
    }
}
