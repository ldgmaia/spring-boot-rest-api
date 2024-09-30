CREATE TABLE IF NOT EXISTS item_conditions
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL,
    created_by      BIGINT UNSIGNED NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (created_by),
    FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS inventory_items
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    category_id     BIGINT UNSIGNED NOT NULL,
    model_id        BIGINT UNSIGNED NOT NULL,
    mpn_id          BIGINT UNSIGNED NOT NULL,
    condition_id    BIGINT UNSIGNED NOT NULL,
    created_by      BIGINT UNSIGNED NOT NULL,
    receiving_items_id BIGINT UNSIGNED NOT NULL,
    by_quantity     BIT NOT NULL,
    quantity        BIGINT UNSIGNED,
    post            VARCHAR(255) NOT NULL,
    serial_number   VARCHAR(255),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (category_id),
    INDEX (model_id),
    INDEX (mpn_id),
    INDEX (condition_id),
    INDEX (created_by),
    INDEX (receiving_items_id),
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (model_id) REFERENCES models (id),
    FOREIGN KEY (mpn_id) REFERENCES mpns (id),
    FOREIGN KEY (condition_id) REFERENCES item_conditions (id), -- Updated reference
    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (receiving_items_id) REFERENCES receiving_items (id) -- Ensure this table exists
);
