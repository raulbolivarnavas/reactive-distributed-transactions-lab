package com.raulbolivar.orders.ports.in;

import com.raulbolivar.orders.model.Command;
import com.raulbolivar.orders.model.Order;
import reactor.core.publisher.Mono;

public interface ICreateOrderSagaUseCase {

    Mono<Order> execute(Command command);
}
