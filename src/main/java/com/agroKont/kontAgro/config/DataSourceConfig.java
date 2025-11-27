package com.agroKont.kontAgro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() throws Exception {
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            throw new IllegalArgumentException("DATABASE_URL no definida");
        }

        // Maneja el formato postgres://usuario:pass@host:port/db
        if (databaseUrl.startsWith("postgres://")) {
            URI dbUri = new URI(databaseUrl);

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            return DataSourceBuilder.create()
                    .url(url)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }

        // Ya está en formato JDBC
        return DataSourceBuilder.create()
                .url(databaseUrl)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
