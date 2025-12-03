package kr.co.manuals.common.api.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "manuals_api_tb")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiEntity {
    @Id
    @Column(name = "api_id")
    private UUID apiId;
    
    @Column(name = "method_type")
    private String methodType;
    
    @Column(name = "api_nm")
    private String apiNm;
    
    @Column(name = "api_dsctn", columnDefinition = "TEXT")
    private String apiDsctn;
    
    @Column(name = "api_url")
    private String apiUrl;
    
    @Column(name = "header", columnDefinition = "TEXT")
    private String header;
    
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;
    
    @Column(name = "response", columnDefinition = "TEXT")
    private String response;
    
    // Base Entity Fields
    @Column(name = "rgtr_id")
    private UUID rgtrId;
    
    @Column(name = "rgtr")
    private String rgtr;
    
    @Column(name = "reg_dt")
    private LocalDateTime regDt;
    
    @Column(name = "mdfr_id")
    private UUID mdfrId;
    
    @Column(name = "mdfr")
    private String mdfr;
    
    @Column(name = "mdfcn_dt")
    private LocalDateTime mdfcnDt;
    
    @PrePersist
    public void prePersist() {
        this.regDt = LocalDateTime.now();
        this.mdfcnDt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.mdfcnDt = LocalDateTime.now();
    }
}
