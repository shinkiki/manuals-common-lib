package kr.co.manuals.common.api.infrastructure.repository;

import kr.co.manuals.common.api.infrastructure.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApiRepository extends JpaRepository<ApiEntity, UUID> {
}
