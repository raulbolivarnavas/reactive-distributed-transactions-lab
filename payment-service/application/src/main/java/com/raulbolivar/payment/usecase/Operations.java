package com.raulbolivar.payment.usecase;

import com.raulbolivar.payment.model.Operation;
import com.raulbolivar.payment.ports.in.IOperationsUseCase;
import com.raulbolivar.payment.ports.out.OperationStore;
import reactor.core.publisher.Mono;

public class Operations implements IOperationsUseCase {
    private final OperationStore store;

    public Operations(OperationStore store) {
        this.store = store;
    }

    @Override
    public Mono<Void> execute(Operation o) {
        if ("PAYMENT_REJECTED".equals(o.scenario()))
            return Mono.error(new IllegalArgumentException("PAYMENT_REJECTED"));
        return store.save(o, "EXECUTED");
    }

    @Override
    public Mono<Void> compensate(Operation o) {
        if ("REFUND_FAILED".equals(o.scenario())) return Mono.error(new IllegalArgumentException("REFUND_FAILED"));
        return store.save(o, "COMPENSATED");
    }
}
