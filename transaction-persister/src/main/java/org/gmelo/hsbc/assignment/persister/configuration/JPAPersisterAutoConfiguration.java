package org.gmelo.hsbc.assignment.persister.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Created by gmelo on 20/08/18.
 */
@Configuration
@Profile("aggregation-persistence")
public class JPAPersisterAutoConfiguration {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.driver-class-name}")
    private String className;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;


    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .driverClassName(className)
                .password(password)
                .url(url)
                .username(username)
                .build();
    }


}
