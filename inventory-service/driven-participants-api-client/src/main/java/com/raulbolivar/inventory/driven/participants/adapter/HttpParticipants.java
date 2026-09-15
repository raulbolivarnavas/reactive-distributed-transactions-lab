package com.raulbolivar.inventory.driven.participants.adapter;

import com.raulbolivar.inventory.model.Order;
import com.raulbolivar.inventory.ports.out.Participants;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class HttpParticipants implements Participants {

    private final WebClient client;
    private final Environment env;

    public HttpParticipants(WebClient builder, Environment env) {
        this.client = builder;
        this.env = env;
    }

    public Mono<Void> action(String service, Order order) {
        return call(service, order, "execute");
    }

    public Mono<Void> compensate(String service, Order order) {
        return call(service, order, "compensate");
    }

    private Mono<Void> call(String service, Order o, String operation) {
        return client.post()
                .uri(env.getRequiredProperty("participants." + service) + "/api/v1/operations/" + operation)
                .header("Saga-Id", o.sagaId().toString())
                .bodyValue(Map.of(
                        "orderId", o.orderId(),
                        "productId", o.command().productId(),
                        "quantity", o.command().quantity(),
                        "amount", o.command().amount(),
                        "scenario", o.command().scenario() == null ? "NONE" : o.command().scenario()
                ))
                .retrieve()
                .bodyToMono(Void.class);
    }
}
