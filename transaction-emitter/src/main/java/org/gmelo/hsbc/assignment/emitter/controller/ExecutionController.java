package org.gmelo.hsbc.assignment.emitter.controller;

import org.gmelo.hsbc.assignment.emitter.model.EmitterStatus;
import org.gmelo.hsbc.assignment.emitter.service.TransactionEmitter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by gmelo on 15/08/18.
 * Controls the execution of the emitter
 */
@RestController
@RequestMapping("/emitter")
public class ExecutionController {

    @Resource
    private TransactionEmitter transactionEmitter;

    /**
     * Handles calls to start the emitter
     */
    @CrossOrigin
    @RequestMapping("/start")
    public void start() {
        transactionEmitter.start();
    }

    /**
     * Handles calls to stop the emitter
     */
    @CrossOrigin
    @RequestMapping("/stop")
    public void stop() {
        transactionEmitter.stop();
    }

    /**
     * Handles calls of the emitter status
     */
    @CrossOrigin
    @RequestMapping("/status")
    public EmitterStatus status() {
        return transactionEmitter.status();
    }

}
