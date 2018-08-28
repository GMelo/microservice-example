package org.gmelo.hsbc.assignment.emitter.service;

import org.gmelo.hsbc.assignment.emitter.model.EmitterStatus;

/**
 * Created by gmelo on 15/08/18.
 */
public interface TransactionEmitter {

    void start();

    void stop();

    EmitterStatus status();
}
