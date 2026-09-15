package com.raulbolivar.payment.ports.in;

import com.raulbolivar.payment.model.Operation;
import reactor.core.publisher.Mono;

public interface IOperationsUseCase {
    Mono<Void> execute(Operation o);

    Mono<Void> compensate(Operation o);
}
