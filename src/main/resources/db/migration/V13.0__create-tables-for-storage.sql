CREATE TABLE IF NOT EXISTS storage_zones (
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name                VARCHAR(255),
    enabled             BIT DEFAULT 1 NOT NULL,
    description         VARCHAR(255),
    created_by          BIGINT UNSIGNED NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (created_by)                REFERENCES users (id),
    UNIQUE (name),
    INDEX (created_by)
);

CREATE TABLE IF NOT EXISTS storage_areas (
    id                              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name                            VARCHAR(255),
    enabled                         BIT DEFAULT 1 NOT NULL,
    description                     VARCHAR(255),
    storage_zone_id                 BIGINT UNSIGNED,
    created_by                      BIGINT UNSIGNED NOT NULL,
    created_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (storage_zone_id)   REFERENCES storage_zones(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by)        REFERENCES users (id),
    INDEX (storage_zone_id),
    INDEX (created_by)
);

CREATE TABLE IF NOT EXISTS storage_locations (
    id                              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name                            VARCHAR(255),
    enabled                         BIT DEFAULT 1 NOT NULL,
    description                     VARCHAR(255),
    storage_area_id                 BIGINT UNSIGNED,
    created_by                      BIGINT UNSIGNED NOT NULL,
    created_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (storage_area_id)   REFERENCES storage_areas(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by)        REFERENCES users (id),
    INDEX (storage_area_id),
    INDEX (created_by)
);

CREATE TABLE IF NOT EXISTS storage_levels (
    id                              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name                            VARCHAR(255),
    enabled                         BIT DEFAULT 1 NOT NULL,
    storage_location_id             BIGINT UNSIGNED,
    created_by                      BIGINT UNSIGNED NOT NULL,
    created_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (storage_location_id)   REFERENCES storage_locations(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by)            REFERENCES users (id),
    INDEX (storage_location_id),
    INDEX (created_by)
);

CREATE TABLE IF NOT EXISTS log_transfer_items (
    id                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    item_id           BIGINT UNSIGNED NOT NULL,
    sender_id         BIGINT UNSIGNED NOT NULL,
    receptor_id       BIGINT UNSIGNED NOT NULL,
    created_by        BIGINT UNSIGNED NOT NULL,
    transfer_status   ENUM('SUCCESS', 'FAILURE') NOT NULL,
    error_message     VARCHAR(255) DEFAULT NULL,
    action_timestamp  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (item_id)       REFERENCES inventory_items(id),
    FOREIGN KEY (sender_id)     REFERENCES users(id),
    FOREIGN KEY (receptor_id)   REFERENCES users(id),
    FOREIGN KEY (created_by)    REFERENCES users(id),
    INDEX (item_id),
    INDEX (sender_id),
    INDEX (receptor_id),
    INDEX (created_by)
);
