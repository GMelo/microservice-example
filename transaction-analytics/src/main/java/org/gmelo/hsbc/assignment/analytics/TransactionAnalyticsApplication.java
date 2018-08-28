package org.gmelo.hsbc.assignment.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * Created by gmelo on 18/08/18.
 */
@SpringBootApplication
@EnableAutoConfiguration
        (exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class, MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class TransactionAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionAnalyticsApplication.class, args);
    }

}
