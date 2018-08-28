package org.gmelo.hsbc.assignment.persister.configuration;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.lang.NonNull;

/**
 * Created by gmelo on 20/08/18.
 */

@Configuration
@Profile("transaction-persistence")
public class MongoPersisterAutoConfiguration extends AbstractMongoConfiguration {


    @NonNull
    @Value("${spring.data.mongodb.host}")
    private String mongoUrl;
    @NonNull
    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;

    /**
     * Return the {@link com.mongodb.MongoClient} instance to connect to. Annotate with {@link Bean} in case you want to expose a
     * {@link com.mongodb.MongoClient} instance to the {@link org.springframework.context.ApplicationContext}.
     *
     * @return
     */
    @Bean
    @Override
    public MongoClient mongoClient() {
        return new MongoClient(mongoUrl);
    }

    /**
     * Return the name of the database to connect to.
     *
     * @return must not be {@literal null}.
     */
    @Bean
    @Override
    protected String getDatabaseName() {
        return mongoDatabase;
    }
}
