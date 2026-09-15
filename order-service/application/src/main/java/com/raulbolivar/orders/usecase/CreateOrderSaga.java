package com.raulbolivar.orders.usecase;

import com.raulbolivar.orders.model.Command;
import com.raulbolivar.orders.model.Order;
import com.raulbolivar.orders.ports.in.ICreateOrderSagaUseCase;
import com.raulbolivar.orders.ports.out.OrderStore;
import com.raulbolivar.orders.ports.out.Participants;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class CreateOrderSaga implements ICreateOrderSagaUseCase {

    private final OrderStore store;
    private final Participants participants;

    public CreateOrderSaga(OrderStore store, Participants participants) {
        this.store = store;
        this.participants = participants;
    }

    @Override
    public Mono<Order> execute(Command command) {
        return Mono.defer(() -> {
            var order = new Order(
                    UUID.randomUUID(), UUID.randomUUID(), command, "PENDING", null
            );

            var completed = new ArrayList<String>();

            return store
                    .save(order).flatMap(saved -> Flux.fromIterable(
                            List.of("inventory", "payment", "shipping")
                    )
                    .concatMap(service -> Mono.defer(() -> participants.action(service, saved))
                            .doOnSuccess(v -> completed.add(service))
                    )
                    .then(Mono.defer(() -> store.save(saved.withStatus("COMPLETED", null))))
                    .onErrorResume(error -> compensate(saved, completed, error)));
        });
    }

    private Mono<Order> compensate(Order order, List<String> completed, Throwable original) {

        var reversed = new ArrayList<>(completed);

        Collections.reverse(reversed);

        var failures = new ArrayList<String>();

        return Flux
                .fromIterable(reversed)
                .concatMap(service -> Mono.defer(() -> participants.compensate(service, order))
                        .onErrorResume(e -> {
                            failures.add(service + ": " + e.getMessage());
                            return Mono.empty();
                        })
                )
                .then(Mono.defer(() -> store.save(
                        order
                                .withStatus(failures.isEmpty() ? "CANCELLED" : "COMPENSATION_FAILED",
                                        original.getMessage() + (
                                                        failures.isEmpty() ? "" : "; " + String.join("; ", failures)
                                        )
                                ))
                        )
                );
    }
}
