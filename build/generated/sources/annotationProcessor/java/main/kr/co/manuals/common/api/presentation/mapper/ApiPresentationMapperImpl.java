package kr.co.manuals.common.api.presentation.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import kr.co.manuals.common.api.application.dto.ApiDiscoveryResult;
import kr.co.manuals.common.api.application.dto.ApiResult;
import kr.co.manuals.common.api.application.dto.EditApiCommand;
import kr.co.manuals.common.api.application.dto.SaveApiCommand;
import kr.co.manuals.common.api.presentation.dto.EditApiRequest;
import kr.co.manuals.common.api.presentation.dto.EditApiResponse;
import kr.co.manuals.common.api.presentation.dto.ListApiResponse;
import kr.co.manuals.common.api.presentation.dto.ReadApiResponse;
import kr.co.manuals.common.api.presentation.dto.SaveApiRequest;
import kr.co.manuals.common.api.presentation.dto.SaveApiResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-03T14:24:01+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21.0.9 (Homebrew)"
)
@Component
public class ApiPresentationMapperImpl implements ApiPresentationMapper {

    @Override
    public SaveApiCommand toSaveCommand(SaveApiRequest request) {
        if ( request == null ) {
            return null;
        }

        String methodType = null;
        String apiNm = null;
        String apiDsctn = null;
        String apiUrl = null;
        String header = null;
        String body = null;
        String response = null;

        methodType = request.methodType();
        apiNm = request.apiNm();
        apiDsctn = request.apiDsctn();
        apiUrl = request.apiUrl();
        header = request.header();
        body = request.body();
        response = request.response();

        SaveApiCommand saveApiCommand = new SaveApiCommand( methodType, apiNm, apiDsctn, apiUrl, header, body, response );

        return saveApiCommand;
    }

    @Override
    public EditApiCommand toEditCommand(EditApiRequest request) {
        if ( request == null ) {
            return null;
        }

        String methodType = null;
        String apiNm = null;
        String apiDsctn = null;
        String apiUrl = null;
        String header = null;
        String body = null;
        String response = null;

        methodType = request.methodType();
        apiNm = request.apiNm();
        apiDsctn = request.apiDsctn();
        apiUrl = request.apiUrl();
        header = request.header();
        body = request.body();
        response = request.response();

        EditApiCommand editApiCommand = new EditApiCommand( methodType, apiNm, apiDsctn, apiUrl, header, body, response );

        return editApiCommand;
    }

    @Override
    public SaveApiResponse toSaveResponse(ApiResult result) {
        if ( result == null ) {
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

        apiId = result.apiId();
        methodType = result.methodType();
        apiNm = result.apiNm();
        apiDsctn = result.apiDsctn();
        apiUrl = result.apiUrl();
        header = result.header();
        body = result.body();
        response = result.response();

        SaveApiResponse saveApiResponse = new SaveApiResponse( apiId, methodType, apiNm, apiDsctn, apiUrl, header, body, response );

        return saveApiResponse;
    }

    @Override
    public EditApiResponse toEditResponse(ApiResult result) {
        if ( result == null ) {
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

        apiId = result.apiId();
        methodType = result.methodType();
        apiNm = result.apiNm();
        apiDsctn = result.apiDsctn();
        apiUrl = result.apiUrl();
        header = result.header();
        body = result.body();
        response = result.response();

        EditApiResponse editApiResponse = new EditApiResponse( apiId, methodType, apiNm, apiDsctn, apiUrl, header, body, response );

        return editApiResponse;
    }

    @Override
    public ReadApiResponse toReadResponse(ApiResult result) {
        if ( result == null ) {
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

        apiId = result.apiId();
        methodType = result.methodType();
        apiNm = result.apiNm();
        apiDsctn = result.apiDsctn();
        apiUrl = result.apiUrl();
        header = result.header();
        body = result.body();
        response = result.response();
        rgtrId = result.rgtrId();
        rgtr = result.rgtr();
        regDt = result.regDt();
        mdfrId = result.mdfrId();
        mdfr = result.mdfr();
        mdfcnDt = result.mdfcnDt();

        ReadApiResponse readApiResponse = new ReadApiResponse( apiId, methodType, apiNm, apiDsctn, apiUrl, header, body, response, rgtrId, rgtr, regDt, mdfrId, mdfr, mdfcnDt );

        return readApiResponse;
    }

    @Override
    public ListApiResponse toListResponse(ApiDiscoveryResult result) {
        if ( result == null ) {
            return null;
        }

        UUID apiId = null;
        String methodType = null;
        String apiNm = null;
        String apiDsctn = null;
        String apiUrl = null;
        boolean registered = false;
        String registerType = null;

        apiId = result.apiId();
        methodType = result.methodType();
        apiNm = result.apiNm();
        apiDsctn = result.apiDsctn();
        apiUrl = result.apiUrl();
        registered = result.registered();
        registerType = result.registerType();

        ListApiResponse listApiResponse = new ListApiResponse( apiId, methodType, apiNm, apiDsctn, apiUrl, registered, registerType );

        return listApiResponse;
    }

    @Override
    public List<ListApiResponse> toListResponses(List<ApiDiscoveryResult> results) {
        if ( results == null ) {
            return null;
        }

        List<ListApiResponse> list = new ArrayList<ListApiResponse>( results.size() );
        for ( ApiDiscoveryResult apiDiscoveryResult : results ) {
            list.add( toListResponse( apiDiscoveryResult ) );
        }

        return list;
    }
}
