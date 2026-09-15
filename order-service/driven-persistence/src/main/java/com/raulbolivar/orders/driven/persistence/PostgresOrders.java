package com.raulbolivar.orders.driven.persistence;

import com.raulbolivar.orders.model.Command;
import com.raulbolivar.orders.model.Order;
import com.raulbolivar.orders.ports.out.OrderStore;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class PostgresOrders implements OrderStore {

    private final DatabaseClient db;

    public PostgresOrders(DatabaseClient db) {
        this.db = db;
    }

    @Override
    public Mono<Order> save(Order o) {

        var spec = db.sql("""
                    INSERT INTO orders(id,saga_id,customer_id,product_id,quantity,amount,scenario,status,failure_reason)
                    VALUES(:id,:saga,:customer,:product,:quantity,:amount,:scenario,:status,:reason)
                        ON CONFLICT(id) DO
                    UPDATE SET status=EXCLUDED.status,failure_reason=EXCLUDED.failure_reason,updated_at=now()
                    """)
                .bind("id", o.orderId())
                .bind("saga", o.sagaId())
                .bind("customer", o.command().customerId())
                .bind("product", o.command().productId())
                .bind("quantity", o.command().quantity())
                .bind("amount", o.command().amount())
                .bind("scenario", o.command().scenario() == null ? "NONE" : o.command().scenario())
                .bind("status", o.status());

        spec = o.failureReason() == null
                ? spec.bindNull("reason", String.class)
                : spec.bind("reason", o.failureReason());

        return spec.fetch()
                .rowsUpdated()
                .thenReturn(o);
    }

    @Override
    public Mono<Order> find(UUID id) {
        return db.sql("SELECT * FROM orders WHERE id=:id")
                .bind("id", id)
                .map((row, meta) -> new Order(
                        row.get("id", UUID.class),
                        row.get("saga_id", UUID.class),
                        new Command(row.get("customer_id", String.class),
                                row.get("product_id", String.class),
                                row.get("quantity", Integer.class),
                                row.get("amount", BigDecimal.class),
                                row.get("scenario", String.class)),
                        row.get("status", String.class),
                        row.get("failure_reason", String.class)))
                .one();
    }
}
