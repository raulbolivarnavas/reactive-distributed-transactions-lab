package com.raulbolivar.orders.model;

import java.util.UUID;

public record Order(
        UUID orderId,
        UUID sagaId,
        Command command,
        String status,
        String failureReason
) {

    public Order withStatus(
            String state,
            String reason
    ) {
        return new Order(orderId, sagaId, command, state, reason);
    }
}
