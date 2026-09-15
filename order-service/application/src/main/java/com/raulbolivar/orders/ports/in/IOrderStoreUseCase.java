package com.raulbolivar.orders.ports.in;

import com.raulbolivar.orders.model.Order;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IOrderStoreUseCase {

    Mono<Order> find(UUID id);
}
