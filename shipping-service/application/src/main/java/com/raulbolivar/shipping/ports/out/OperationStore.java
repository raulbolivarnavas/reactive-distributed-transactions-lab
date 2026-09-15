package com.raulbolivar.shipping.ports.out;

import com.raulbolivar.shipping.model.Operation;
import reactor.core.publisher.Mono;

public interface OperationStore {

    Mono<Void> save(Operation operation, String status);
}
