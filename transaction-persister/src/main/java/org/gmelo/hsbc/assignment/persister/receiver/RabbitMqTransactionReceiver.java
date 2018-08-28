package org.gmelo.hsbc.assignment.persister.receiver;

import org.gmelo.hsbc.assignment.common.model.InternalTransactional;
import org.gmelo.hsbc.assignment.persister.repository.MongoTransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.gmelo.hsbc.assignment.common.factory.InternalModelFactory.byteArrayToInternalTransactionalTransaction;
import static org.gmelo.hsbc.assignment.persister.repository.model.TransactionDAO.fromInternalTransactional;

/**
 * Created by gmelo on 16/08/18.
 * persists messages to MongoTransactionRepo
 */

@Component
@RabbitListener(queues = "org.gmelo.hsbc.transaction")
@Profile("transaction-persistence")
public class RabbitMqTransactionReceiver {

    private final Logger logger = LoggerFactory.getLogger(RabbitMqTransactionReceiver.class);
    @Resource
    private MongoTransactionRepo mongoTransactionRepo;

    @RabbitHandler
    public void receive(byte[] message) {
        try {
            InternalTransactional internalTransactional = byteArrayToInternalTransactionalTransaction(message);
            mongoTransactionRepo.save(fromInternalTransactional(internalTransactional));
            //in real life this would be trace
            logger.info("Received {}", internalTransactional);
        } catch (Exception e) {
            logger.error("Exception consuming", e);
            //this exception makes the message go to the dead letter queue
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

}
