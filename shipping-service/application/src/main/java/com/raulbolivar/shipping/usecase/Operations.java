package com.raulbolivar.shipping.usecase;

import com.raulbolivar.shipping.model.Operation;
import com.raulbolivar.shipping.ports.in.IOperationsUseCase;
import com.raulbolivar.shipping.ports.out.OperationStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Operations implements IOperationsUseCase {
    private final OperationStore store;

    public Operations(OperationStore store) {
        this.store = store;
    }

    @Override
    public Mono<Void> execute(Operation o) {
        if ("SHIPPING_UNAVAILABLE".equals(o.scenario()) || "REFUND_FAILED".equals(o.scenario()) || "RELEASE_FAILED".equals(o.scenario()))
            return Mono.error(new IllegalArgumentException("SHIPPING_UNAVAILABLE"));
        return store.save(o, "EXECUTED");
    }

    @Override
    public Mono<Void> compensate(Operation o) {
        if ("SHIPPING_CANCEL_FAILED".equals(o.scenario()))
            return Mono.error(new IllegalArgumentException("SHIPPING_CANCEL_FAILED"));
        return store.save(o, "COMPENSATED");
    }
}
