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
@Table(name = "manuals_event_tb", uniqueConstraints = {
        @UniqueConstraint(name = "uk_event_key", columnNames = {"event_key"})
})
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Comment("이벤트")
public class EventEntity {

    public enum DispatchType {
        SYNC,
        ASYNC
    }

    @Id
    @Column(name = "event_id", nullable = false, updatable = false)
    @Comment("이벤트ID")
    private UUID eventId;

    @Column(name = "event_key", nullable = false, length = 255)
    @Comment("이벤트 키(클래스명)")
    @NotNull(message = "이벤트 키는 필수값입니다.")
    private String eventKey;

    @Column(name = "name", nullable = false, length = 255)
    @Comment("이벤트 명")
    @NotNull(message = "이벤트 명은 필수값입니다.")
    private String name;

    @Lob
    @Column(name = "event_expln")
    @Comment("이벤트 설명")
    private String eventExpln;

    @Column(name = "use_yn", nullable = false, length = 1)
    @Comment("사용여부")
    @NotNull(message = "사용여부는 필수값입니다.")
    private String useYn;

    @Enumerated(EnumType.STRING)
    @Column(name = "dispatch_type", nullable = false, length = 32)
    @Comment("디스패치 방식(SYNC|ASYNC)")
    private DispatchType dispatchType = DispatchType.SYNC;

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
