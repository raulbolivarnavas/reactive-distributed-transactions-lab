package com.raulbolivar.inventory.usecase;

import com.raulbolivar.inventory.model.Operation;
import com.raulbolivar.inventory.ports.in.IOperationsUseCase;
import com.raulbolivar.inventory.ports.out.OperationStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Operations implements IOperationsUseCase {
    private final OperationStore store;

    public Operations(OperationStore store) {
        this.store = store;
    }

    public Mono<Void> execute(Operation o) {
        if ("INVENTORY_UNAVAILABLE".equals(o.scenario()))
            return Mono.error(new IllegalArgumentException("INVENTORY_UNAVAILABLE"));
        return store.save(o, "EXECUTED");
    }

    public Mono<Void> compensate(Operation o) {
        if ("RELEASE_FAILED".equals(o.scenario())) return Mono.error(new IllegalArgumentException("RELEASE_FAILED"));
        return store.save(o, "COMPENSATED");
    }
}
