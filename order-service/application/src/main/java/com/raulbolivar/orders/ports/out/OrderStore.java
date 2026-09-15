package com.raulbolivar.orders.ports.out;

import com.raulbolivar.orders.model.Order;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderStore {

    Mono<Order> save(Order order);

    Mono<Order> find(UUID id);
}
