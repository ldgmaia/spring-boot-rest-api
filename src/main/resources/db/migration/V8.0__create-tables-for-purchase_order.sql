CREATE TABLE IF NOT EXISTS purchase_orders (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    status VARCHAR(255) NOT NULL,
    po_number VARCHAR(255) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    qbo_id BIGINT UNSIGNED NOT NULL,
    qbo_created_at TIMESTAMP,
    qbo_updated_at TIMESTAMP,
    suppliers_id BIGINT UNSIGNED NOT NULL,
    received VARCHAR(255) NOT NULL,
    watching_po VARCHAR(255) NOT NULL,
    last_received_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE (po_number),
    UNIQUE (qbo_id),
    INDEX (suppliers_id),
    FOREIGN KEY (suppliers_id) REFERENCES suppliers (id)
);

CREATE TABLE IF NOT EXISTS purchase_order_items (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(768),
    quantity DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    qbo_item_id BIGINT UNSIGNED NOT NULL,
    qbo_purchase_order_item_id BIGINT UNSIGNED NOT NULL,
    purchase_orders_id BIGINT UNSIGNED NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
--    UNIQUE (purchase_orders_id, qbo_purchase_order_item_id, description), -- Issue with MySQL
    INDEX (purchase_orders_id),
    FOREIGN KEY (purchase_orders_id) REFERENCES purchase_orders (id)
);
