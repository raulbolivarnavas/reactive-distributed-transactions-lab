package com.raulbolivar.orders.model;

import java.math.BigDecimal;

public record Command(
        String customerId,
        String productId,
        int quantity,
        BigDecimal amount,
        String scenario
) {

    public Command {
        if (customerId == null
                || customerId.isBlank()
                || productId == null
                || productId.isBlank()
                || quantity <= 0
                || amount == null
                || amount.signum() <= 0
                || amount.scale() > 2
        )
            throw new IllegalArgumentException("customerId, productId, positive quantity and amount (maximum two decimals) required");
    }
}
