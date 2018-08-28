package org.gmelo.hsbc.assignment.receiver.controller;

import org.gmelo.hsbc.assignment.receiver.service.TransactionQueuePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by gmelo on 15/08/18.
 */

@RestController
@RequestMapping("/receiver")
public class TransactionReceiverController {

    private final Logger logger = LoggerFactory.getLogger(TransactionReceiverController.class);
    @Resource
    private TransactionQueuePublisher transactionQueuePublisher;

    @RequestMapping(value = "/endpoint", method = RequestMethod.POST)
    public void handleExternalTransaction(@RequestBody Map<String, Object> data) {
        transactionQueuePublisher.publishTransaction(data);
    }

}
