package org.gmelo.hsbc.assignment.persister.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by gmelo on 16/08/18.
 * All queues used to route and consume messages.
 */

@Configuration
public class PersisterConfiguration {

    @Value("${org.gmelo.hsbc.persistence.queue}")
    public String transactionPersistenceQueueName;
    @Value("${org.gmelo.hsbc.aggregate.queue}")
    public String aggregationPersistenceQueueName;
    @Value("${org.gmelo.hsbc.transaction.fanout}")
    public String transactionFanout;
    @Value("${org.gmelo.hsbc.aggregate.fanout}")
    public String aggregationFanout;

    @Bean
    public Queue transactionPersistenceQueue(Queue transactionPersistenceDeadLetterQueue) {
        return QueueBuilder.durable(transactionPersistenceQueueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key",
                        transactionPersistenceDeadLetterQueue.getName()).build();
    }

    @Bean
    public Queue transactionPersistenceDeadLetterQueue() {
        return QueueBuilder.durable(transactionPersistenceQueueName + ".dead.letter").build();
    }

    @Bean
    public Queue aggregationPersistenceQueue(Queue aggregationPersistenceDeadLetterQueue) {
        return QueueBuilder.durable(aggregationPersistenceQueueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", aggregationPersistenceDeadLetterQueue.getName()).build();
    }

    @Bean
    public Queue aggregationPersistenceDeadLetterQueue() {
        return QueueBuilder.durable(aggregationPersistenceQueueName + ".dead.letter").build();
    }


    @Bean
    public Binding bindingToAggregate(@Qualifier("aggregationFanout") FanoutExchange aggregationFanout,
                                      @Qualifier("aggregationPersistenceQueue") Queue aggregationPersistenceQueue) {
        return BindingBuilder.bind(aggregationPersistenceQueue).to(aggregationFanout);
    }

    @Bean
    public Binding bindingToTransaction(@Qualifier("transactionFanout") FanoutExchange transactionFanout,
                                        @Qualifier("transactionPersistenceQueue") Queue transactionPersistenceQueue) {
        return BindingBuilder.bind(transactionPersistenceQueue).to(transactionFanout);
    }

    @Bean
    public FanoutExchange transactionFanout() {
        return new FanoutExchange(transactionFanout);
    }

    @Bean
    public FanoutExchange aggregationFanout() {
        return new FanoutExchange(aggregationFanout);
    }
}
