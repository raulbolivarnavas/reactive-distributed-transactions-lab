package com.raulbolivar.payment.driven.persistence;

import com.raulbolivar.payment.model.Operation;
import com.raulbolivar.payment.ports.out.OperationStore;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PostgresOperations implements OperationStore {
    private final DatabaseClient db;

    public PostgresOperations(DatabaseClient db) {
        this.db = db;
    }

    public Mono<Void> save(Operation o, String status) {
        return db.sql("INSERT INTO operations(order_id,product_id,quantity,amount,status) VALUES(:id,:product,:quantity,:amount,:status) ON CONFLICT(order_id) DO UPDATE SET status=EXCLUDED.status,updated_at=now()")
                .bind("id", o.orderId()).bind("product", o.productId()).bind("quantity", o.quantity()).bind("amount", o.amount()).bind("status", status).fetch().rowsUpdated().then();
    }
}
