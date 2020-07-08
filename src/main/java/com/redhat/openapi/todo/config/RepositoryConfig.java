package com.redhat.openapi.todo.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"com.redhat.openapi.todo.dao"})
@EntityScan(basePackages = "com.redhat.openapi.todo.models")
@EnableTransactionManagement
public class RepositoryConfig {
    /**
     * This is here so the json serialization doesn't force
     * a lazy fetch on our domain objects
     */
    @Bean
    public Module datatypeHibernateModule() {
        return new Hibernate5Module();
    }
}