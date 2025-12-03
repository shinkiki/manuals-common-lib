package kr.co.manuals.common.api;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ComponentScan(basePackages = "kr.co.manuals.common.api")
@EntityScan(basePackages = "kr.co.manuals.common.api.infrastructure.entity")
@EnableJpaRepositories(basePackages = "kr.co.manuals.common.api.infrastructure.repository")
public class ApiAutoConfiguration {
}
