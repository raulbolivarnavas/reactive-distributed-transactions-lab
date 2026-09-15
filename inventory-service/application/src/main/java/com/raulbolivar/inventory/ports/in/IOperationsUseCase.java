package com.raulbolivar.inventory.ports.in;

import com.raulbolivar.inventory.model.Operation;
import reactor.core.publisher.Mono;

public interface IOperationsUseCase {
    Mono<Void> execute(Operation o);

    Mono<Void> compensate(Operation o);
}
