package org.gmelo.hsbc.assignment.emitter.service;


import org.gmelo.hsbc.assignment.emitter.model.CreditCardTransaction;
import org.gmelo.hsbc.assignment.emitter.model.CreditCardTransactionFactory;
import org.gmelo.hsbc.assignment.emitter.model.EmitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;

/**
 * Created by gmelo on 14/08/18.
 * <p>
 * The Emitter generates cc transactions to simulate a external system publishing into the
 * assignment, because of this we use a different model, and publish though rest.
 */
@Service
public class TransactionEmitterService implements TransactionEmitter {

    private final Logger logger = LoggerFactory.getLogger(TransactionEmitterService.class);

    @Value("${org.gmelo.hsbc.emitter.interval}")
    private long interval;
    @Value("${org.gmelo.hsbc.emitter.receiver.endpoint}")
    private String receiverEndpoint;
    @Resource
    private CreditCardTransactionFactory creditCardTransactionFactory;
    //template to connect to the
    private final RestTemplate restTemplate = new RestTemplate();
    // Scheduler to execute the tasks
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    // Executor service to ensure that if the threads are taking longer to run than the scheduled interval the schedule is still being met
    private final Executor executionRunner = Executors.newFixedThreadPool(2);
    //control of the current Running Thread
    private ScheduledFuture<?> future;

    @Override
    public void start() {

        if (future == null || future.isCancelled() || future.isDone()) {

            future = scheduler.scheduleAtFixedRate(() ->
                    //Wrap the execution in a thread pool
                    executionRunner.execute(() -> {
                        CreditCardTransaction cc = creditCardTransactionFactory.createTransaction();
                        //in real life this would be trace
                        logger.info("Generated transaction {}", cc);
                        try {
                            restTemplate.postForObject(receiverEndpoint, cc, ResponseEntity.class);

                        } catch (Exception e) {
                            logger.error("Exception {} trying to send cc to {} ", e.getMessage(), receiverEndpoint);
                            //In real life we would store this, but was we are using random transactions we don't care.
                        }
                    })
                    , 0, interval, MILLISECONDS);
            logger.info("Started Emitter");
        } else {
            logger.warn("Already Running");
        }
    }

    @Override
    public void stop() {
        if (future == null || future.isCancelled()) {
            logger.warn("Emitter has not been initialized yet");
        } else {
            future.cancel(false);
            logger.info("Stopped emitter");
        }
    }

    @Override
    public EmitterStatus status() {
        return (future == null || future.isCancelled() || future.isDone()) ? EmitterStatus.NOT_RUNNING : EmitterStatus.RUNNING;
    }
}
