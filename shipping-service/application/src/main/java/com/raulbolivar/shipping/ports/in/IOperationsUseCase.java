package com.raulbolivar.shipping.ports.in;

import com.raulbolivar.shipping.model.Operation;
import reactor.core.publisher.Mono;

public interface IOperationsUseCase {

    Mono<Void> execute(Operation o);

    Mono<Void> compensate(Operation o);
}
