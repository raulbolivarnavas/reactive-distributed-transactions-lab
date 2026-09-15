package com.raulbolivar.payment.ports.out;

import com.raulbolivar.payment.model.Operation;
import reactor.core.publisher.Mono;

public interface OperationStore {
    Mono<Void> save(Operation operation, String status);
}
