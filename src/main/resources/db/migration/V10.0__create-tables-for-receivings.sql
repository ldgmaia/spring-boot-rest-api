CREATE TABLE IF NOT EXISTS receivings
(
    id                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    tracking_lading   VARCHAR(255),
    carrier           VARCHAR(255),
    type              VARCHAR(255),
    suppliers_id      BIGINT UNSIGNED NOT NULL,
    identifier        VARCHAR(255),
    parcels           VARCHAR(255),
    notes             VARCHAR(255),
    created_by        BIGINT UNSIGNED NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (suppliers_id),
    INDEX (created_by),
    FOREIGN KEY (suppliers_id) REFERENCES suppliers (id),
    FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS receiving_items
(
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    receiving_id        BIGINT UNSIGNED NOT NULL,
    purchase_order_item_id        BIGINT UNSIGNED NOT NULL,
    description         VARCHAR(255),
    quantity_to_receive BIGINT,
    quantity_received   BIGINT,
    created_by          BIGINT UNSIGNED NOT NULL,
    receivable_item     BIT NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (receiving_id),
    INDEX (purchase_order_item_id),
    INDEX (created_by),
    FOREIGN KEY (receiving_id) REFERENCES receivings (id),
    FOREIGN KEY (purchase_order_item_id) REFERENCES purchase_order_items (id),
    FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS receiving_pictures
(
    id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    receiving_id BIGINT UNSIGNED NOT NULL,
    file_id      BIGINT UNSIGNED NOT NULL,
    created_by   BIGINT UNSIGNED NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (receiving_id),
    INDEX (file_id),
    INDEX (created_by),
    FOREIGN KEY (receiving_id) REFERENCES receivings (id),
    FOREIGN KEY (file_id) REFERENCES files (id),
    FOREIGN KEY (created_by) REFERENCES users (id)
);


CREATE TABLE IF NOT EXISTS receiving_additional_items
(
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    receiving_id        BIGINT UNSIGNED NOT NULL,
    description         VARCHAR(255),
    quantity_received   BIGINT,
    created_by          BIGINT UNSIGNED NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX (receiving_id),
    INDEX (created_by),
    FOREIGN KEY (receiving_id) REFERENCES receivings (id),
    FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS receiving_additional_items
(
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    receiving_id        BIGINT UNSIGNED NOT NULL,
    description         VARCHAR(255),
    quantity_received   BIGINT DEFAULT 0,
    created_by          BIGINT UNSIGNED NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (receiving_id),
    INDEX (created_by),
    FOREIGN KEY (receiving_id) REFERENCES receivings (id),
    FOREIGN KEY (created_by) REFERENCES users (id)
);