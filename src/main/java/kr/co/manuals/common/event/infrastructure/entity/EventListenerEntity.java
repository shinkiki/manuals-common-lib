package kr.co.manuals.common.event.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "manuals_event_listener_tb", indexes = {
        @Index(name = "idx_event_listener_event", columnList = "event_id"),
        @Index(name = "idx_event_listener_key_bean_method", columnList = "event_key, bean_name, method_name")
})
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Comment("이벤트리스너")
public class EventListenerEntity {

    @Id
    @Column(name = "listener_id", nullable = false, updatable = false)
    @Comment("리스너ID")
    private UUID listenerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @Comment("이벤트")
    private EventEntity event;

    @Column(name = "event_key", nullable = false, length = 255)
    @Comment("이벤트 키(FQCN)")
    @NotNull(message = "이벤트 키는 필수값입니다.")
    private String eventKey;

    @Column(name = "kind", nullable = false, length = 20)
    @Comment("구독 종류(HANDLER|ANNOTATION|INTERFACE)")
    private String kind;

    @Column(name = "bean_name", nullable = false, length = 255)
    @Comment("빈명")
    private String beanName;

    @Column(name = "bean_class", nullable = false, length = 512)
    @Comment("빈클래스")
    private String beanClass;

    @Column(name = "method_name", nullable = false, length = 255)
    @Comment("메서드명")
    private String methodName;

    @Column(name = "use_yn", nullable = false, length = 1)
    @Comment("사용여부")
    private String useYn;

    @Column(columnDefinition = "TEXT")
    @Comment("설명")
    private String dsctn;

    // 감사 필드
    @CreatedBy
    @Column(name = "rgtr_id", updatable = false)
    private UUID rgtrId;

    @Column(name = "rgtr", length = 100, updatable = false)
    private String rgtr;

    @CreatedDate
    @Column(name = "reg_dt", updatable = false)
    private LocalDateTime regDt;

    @LastModifiedBy
    @Column(name = "mdfr_id")
    private UUID mdfrId;

    @Column(name = "mdfr", length = 100)
    private String mdfr;

    @LastModifiedDate
    @Column(name = "mdfcn_dt")
    private LocalDateTime mdfcnDt;
}
