package ua.alisasira.rest;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class RepositoryTestConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean(initMethod = "migrate", destroyMethod = "clean")
    public Flyway flyway() {
        return Flyway.configure().dataSource(dataSource).baselineOnMigrate(Boolean.TRUE).load();
    }
}