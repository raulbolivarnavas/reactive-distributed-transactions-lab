package com.raulbolivar.orders.driver.api;

import com.raulbolivar.orders.model.Command;
import com.raulbolivar.orders.model.Order;
import com.raulbolivar.orders.ports.in.ICreateOrderSagaUseCase;
import com.raulbolivar.orders.ports.out.OrderStore;
import com.raulbolivar.orders.usecase.CreateOrderSaga;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApi {

    private final ICreateOrderSagaUseCase saga;
    private final OrderStore store;

    public OrderApi(CreateOrderSaga saga, OrderStore store) {
        this.saga = saga;
        this.store = store;
    }

    @PostMapping
    public Mono<ResponseEntity<Order>> create(@RequestBody Command command) {
        return saga.execute(command)
                .map(o -> ResponseEntity.status(201).body(o));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Order>> get(@PathVariable("id") UUID id) {
        return store.find(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
