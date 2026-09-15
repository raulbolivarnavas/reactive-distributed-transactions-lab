package com.raulbolivar.inventory.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Operation(UUID orderId, String productId, int quantity, BigDecimal amount, String scenario) {
    public Operation {
        if (orderId == null || productId == null || productId.isBlank() || quantity <= 0 || amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("Invalid operation");
    }
}
