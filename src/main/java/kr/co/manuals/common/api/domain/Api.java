package kr.co.manuals.common.api.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * API Domain Model
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Api {
    private UUID apiId;
    private String methodType;
    private String apiNm;
    private String apiDsctn;
    private String apiUrl;
    private String header;
    private String body;
    private String response;
    
    // Base fields
    private UUID rgtrId;
    private String rgtr;
    private LocalDateTime regDt;
    private UUID mdfrId;
    private String mdfr;
    private LocalDateTime mdfcnDt;
    
    public static Api create(String methodType, String apiNm, String apiDsctn, String apiUrl, 
                             String header, String body, String response) {
        Api api = new Api();
        api.apiId = UUID.randomUUID();
        api.methodType = methodType;
        api.apiNm = apiNm;
        api.apiDsctn = apiDsctn;
        api.apiUrl = apiUrl;
        api.header = header;
        api.body = body;
        api.response = response;
        return api;
    }
}
