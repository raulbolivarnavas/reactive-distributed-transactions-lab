package com.raulbolivar.orders.usecase;

import com.raulbolivar.orders.model.Command;
import com.raulbolivar.orders.model.Order;
import com.raulbolivar.orders.ports.out.OrderStore;
import com.raulbolivar.orders.ports.out.Participants;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CreateOrderSagaTest {
    private void scenario(String failed, String compensationFailure, String expected, List<String> callsExpected) {
        var calls = new ArrayList<String>();

        OrderStore store = new OrderStore() {
            public Mono<Order> save(Order o) {
                return Mono.just(o);
            }

            public Mono<Order> find(UUID id) {
                return Mono.empty();
            }
        };

        Participants participants = new Participants() {
            public Mono<Void> action(String s, Order o) {
                return Mono.defer(() -> {
                    calls.add(s);
                    return s.equals(failed) ? Mono.error(new RuntimeException("failure")) : Mono.empty();
                });
            }

            public Mono<Void> compensate(String s, Order o) {
                return Mono.defer(() -> {
                    calls.add("undo:" + s);
                    return s.equals(compensationFailure) ? Mono.error(new RuntimeException("undo failed")) : Mono.empty();
                });
            }
        };
        var saga = new CreateOrderSaga(store, participants);
        StepVerifier.create(saga.execute(new Command("CUS-001", "PROD-001", 2, new BigDecimal("150.00"), "NONE"))).assertNext(o -> assertThat(o.status()).isEqualTo(expected)).verifyComplete();
        assertThat(calls).containsExactlyElementsOf(callsExpected);
    }

    @Test
    void completes() {
        scenario("", "", "COMPLETED", List.of("inventory", "payment", "shipping"));
    }

    @Test
    void inventoryFailure() {
        scenario("inventory", "", "CANCELLED", List.of("inventory"));
    }

    @Test
    void paymentFailure() {
        scenario("payment", "", "CANCELLED", List.of("inventory", "payment", "undo:inventory"));
    }

    @Test
    void shippingFailure() {
        scenario("shipping", "", "CANCELLED", List.of("inventory", "payment", "shipping", "undo:payment", "undo:inventory"));
    }

    @Test
    void refundFailureStillReleasesInventory() {
        scenario("shipping", "payment", "COMPENSATION_FAILED", List.of("inventory", "payment", "shipping", "undo:payment", "undo:inventory"));
    }

    @Test
    void releaseFailure() {
        scenario("payment", "inventory", "COMPENSATION_FAILED", List.of("inventory", "payment", "undo:inventory"));
    }

    @Test
    void rejectsInvalidInput() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> new Command("", "P", 0, BigDecimal.ONE, null));
    }
}
