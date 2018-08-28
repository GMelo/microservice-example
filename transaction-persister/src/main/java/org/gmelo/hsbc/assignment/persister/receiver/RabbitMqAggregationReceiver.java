package org.gmelo.hsbc.assignment.persister.receiver;

import org.gmelo.hsbc.assignment.common.model.AggregatedTransaction;
import org.gmelo.hsbc.assignment.persister.repository.model.AggregationDAO;
import org.gmelo.hsbc.assignment.persister.repository.MySQLAggregatedTransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.gmelo.hsbc.assignment.common.factory.InternalModelFactory.byteArrayToAggregatedTransaction;

/**
 * Created by gmelo on 16/08/18.
 * persists messages to MySQLAggregatedTransactionRepo
 */

@Profile("aggregation-persistence")
@Component
@RabbitListener(queues = "org.gmelo.hsbc.aggregation")
public class RabbitMqAggregationReceiver {

    private Logger logger = LoggerFactory.getLogger(RabbitMqAggregationReceiver.class);

    @Resource
    private MySQLAggregatedTransactionRepo mySQLAggregatedTransactionRepo;

    @RabbitHandler
    public void receive(byte[] message) {
        try {
            AggregatedTransaction aggregatedTransaction = byteArrayToAggregatedTransaction(message);
            mySQLAggregatedTransactionRepo.save(AggregationDAO.fromAggregatedTransaction(aggregatedTransaction));
            //in real life this would be trace
            logger.info("Received {}", AggregationDAO.fromAggregatedTransaction(aggregatedTransaction));
        } catch (Exception e) {
            logger.error("Exception consuming", e);
            //this exception makes the message go to the dead letter queue
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
