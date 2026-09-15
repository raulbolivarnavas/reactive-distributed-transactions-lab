CREATE TABLE IF NOT EXISTS orders(
    id UUID      PRIMARY KEY,
    saga_id UUID NOT NULL,
    customer_id  VARCHAR(100) NOT NULL,
    product_id   VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL CHECK(quantity>0),
    amount   NUMERIC(19,2) NOT NULL,
    scenario VARCHAR(100),
    status   VARCHAR(50) NOT NULL,
    failure_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
