package com.raulbolivar.inventory.ports.out;

import com.raulbolivar.inventory.model.Operation;
import reactor.core.publisher.Mono;

public interface OperationStore {
    Mono<Void> save(Operation operation, String status);
}
