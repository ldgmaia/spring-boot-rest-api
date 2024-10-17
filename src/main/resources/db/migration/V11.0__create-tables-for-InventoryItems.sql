CREATE TABLE IF NOT EXISTS locations
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


CREATE TABLE IF NOT EXISTS item_statuses
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
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    category_id         BIGINT UNSIGNED NOT NULL,
    model_id            BIGINT UNSIGNED NOT NULL,
    mpn_id              BIGINT UNSIGNED,
    created_by          BIGINT UNSIGNED NOT NULL,
    receiving_items_id  BIGINT UNSIGNED NOT NULL,
    location_id         BIGINT UNSIGNED NOT NULL,
    post                VARCHAR(255)    NOT NULL,
    serial_number       VARCHAR(255),
    grade               VARCHAR(255),
    item_conditions_id  BIGINT UNSIGNED NOT NULL,
    item_statuses_id    BIGINT UNSIGNED NOT NULL,
    rbid                VARCHAR(255)    NOT NULL,
    type                VARCHAR(255)    NOT NULL,
    cost                DECIMAL(10, 2),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (category_id),
    INDEX (model_id),
    INDEX (mpn_id),
    INDEX (item_conditions_id),
    INDEX (item_statuses_id),
    INDEX (created_by),
    INDEX (receiving_items_id),
    INDEX (location_id),
    INDEX (rbid),
    FOREIGN KEY (category_id)           REFERENCES categories (id),
    FOREIGN KEY (model_id)              REFERENCES models (id),
    FOREIGN KEY (mpn_id)                REFERENCES mpns (id),
    FOREIGN KEY (item_conditions_id)    REFERENCES item_conditions (id),
    FOREIGN KEY (item_statuses_id)      REFERENCES item_statuses (id),
    FOREIGN KEY (created_by)            REFERENCES users (id),
    FOREIGN KEY (receiving_items_id)    REFERENCES receiving_items (id),
    FOREIGN KEY (location_id)           REFERENCES locations (id)
);
