package kr.co.manuals.common.api.application.mapper;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import kr.co.manuals.common.api.application.dto.ApiResult;
import kr.co.manuals.common.api.application.dto.EditApiCommand;
import kr.co.manuals.common.api.application.dto.SaveApiCommand;
import kr.co.manuals.common.api.domain.Api;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T14:24:01+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21.0.9 (Homebrew)"
)
@Component
public class ApiApplicationMapperImpl implements ApiApplicationMapper {

    @Override
    public Api toDomain(SaveApiCommand command) {
        if ( command == null ) {
            return null;
        }

        String methodType = null;
        String apiNm = null;
        String apiDsctn = null;
        String apiUrl = null;
        String header = null;
        String body = null;
        String response = null;

        methodType = command.methodType();
        apiNm = command.apiNm();
        apiDsctn = command.apiDsctn();
        apiUrl = command.apiUrl();
        header = command.header();
        body = command.body();
        response = command.response();

        UUID apiId = null;
        UUID rgtrId = null;
        String rgtr = null;
        LocalDateTime regDt = null;
        UUID mdfrId = null;
        String mdfr = null;
        LocalDateTime mdfcnDt = null;

        Api api = new Api( apiId, methodType, apiNm, apiDsctn, apiUrl, header, body, response, rgtrId, rgtr, regDt, mdfrId, mdfr, mdfcnDt );

        return api;
    }

    @Override
    public ApiResult toResult(Api api) {
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

        ApiResult apiResult = new ApiResult( apiId, methodType, apiNm, apiDsctn, apiUrl, header, body, response, rgtrId, rgtr, regDt, mdfrId, mdfr, mdfcnDt );

        return apiResult;
    }

    @Override
    public Api applyEdit(Api api, EditApiCommand command) {
        if ( command == null ) {
            return api;
        }

        api.setMethodType( command.methodType() );
        api.setApiNm( command.apiNm() );
        api.setApiDsctn( command.apiDsctn() );
        api.setApiUrl( command.apiUrl() );
        api.setHeader( command.header() );
        api.setBody( command.body() );
        api.setResponse( command.response() );

        return api;
    }
}
