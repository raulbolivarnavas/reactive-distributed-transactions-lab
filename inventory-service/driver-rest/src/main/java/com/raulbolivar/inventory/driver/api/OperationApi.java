package com.raulbolivar.inventory.driver.api;

import com.raulbolivar.inventory.model.Operation;
import com.raulbolivar.inventory.ports.in.IOperationsUseCase;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/operations")
public class OperationApi {

    private final IOperationsUseCase operations;

    public OperationApi(IOperationsUseCase operations) {
        this.operations = operations;
    }

    @PostMapping("/execute")
    public Mono<Void> execute(@RequestBody Operation o) {
        return operations.execute(o);
    }

    @PostMapping("/compensate")
    public Mono<Void> compensate(@RequestBody Operation o) {
        return operations.compensate(o);
    }
}
