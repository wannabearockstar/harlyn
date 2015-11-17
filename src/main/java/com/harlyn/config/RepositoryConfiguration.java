package com.harlyn.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by wannabe on 17.11.15.
 */
@Configuration
@EntityScan(basePackages = {"com.harlyn.domain"})
@EnableJpaRepositories(basePackages = {"com.harlyn.repository"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}
