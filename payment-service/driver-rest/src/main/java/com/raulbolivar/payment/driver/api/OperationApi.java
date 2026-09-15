package com.raulbolivar.payment.driver.api;

import com.raulbolivar.payment.model.Operation;
import com.raulbolivar.payment.ports.in.IOperationsUseCase;
import com.raulbolivar.payment.ports.out.OperationStore;
import com.raulbolivar.payment.usecase.Operations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/operations")
public class OperationApi {

    private final IOperationsUseCase operations;

    public OperationApi(OperationStore store) {
        this.operations = new Operations(store);
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
