package com.raulbolivar.orders.ports.out;

import com.raulbolivar.orders.model.Order;
import reactor.core.publisher.Mono;

public interface Participants {

    Mono<Void> action(String service, Order order);

    Mono<Void> compensate(String service, Order order);
}
