CREATE TABLE IF NOT EXISTS receivings
(
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    tracking_lading VARCHAR(255),
    carrier         VARCHAR(255),
    type            VARCHAR(255),
    suppliers_id    BIGINT UNSIGNED NOT NULL,
    identifier      BIGINT,
    parcels         VARCHAR(255),
    notes           VARCHAR(255),
    created_by      BIGINT UNSIGNED,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (created_by),
    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (suppliers_id) REFERENCES suppliers (id)
);

CREATE TABLE IF NOT EXISTS receiving_items
(
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    receiving_id        BIGINT,
    description         VARCHAR(255),
    quantity_to_receive BIGINT,
    quantity_received   BIGINT,
    created_by          BIGINT UNSIGNED,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (created_by),
    INDEX (receiving_id),
    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (receiving_id) REFERENCES receivings (id)
);

CREATE TABLE IF NOT EXISTS receiving_pictures
(
    id              BIGINT     NOT NULL AUTO_INCREMENT,
    receiving_id    BIGINT,
    picture_content MEDIUMBLOB NOT NULL,
    created_by      BIGINT UNSIGNED,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (created_by),
    INDEX (receiving_id),

    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (receiving_id) REFERENCES receivings (id)
);