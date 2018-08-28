package org.gmelo.hsbc.assignment.persister;

import org.gmelo.hsbc.assignment.persister.configuration.JPAPersisterAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by gmelo on 16/08/18.
 */

@SpringBootApplication
@EnableAutoConfiguration
        (exclude = {
               DataSourceAutoConfiguration.class,
             //  DataSourceTransactionManagerAutoConfiguration.class,
                MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class,
             // HibernateJpaAutoConfiguration.class
        })
public class TransactionPersisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionPersisterApplication.class, args);
    }
}
