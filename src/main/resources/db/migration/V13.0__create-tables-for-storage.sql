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

CREATE TABLE IF NOT EXISTS item_transfer_log (
    id                                      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    inventory_item_id                       BIGINT UNSIGNED NOT NULL,
    from_storage_level_id                   BIGINT UNSIGNED NOT NULL,
    to_storage_level_id                     BIGINT UNSIGNED NOT NULL,
    created_by                              BIGINT UNSIGNED NOT NULL,
    transfer_status                         VARCHAR(255),
    message                                 VARCHAR(255),
    created_at                              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (inventory_item_id)         REFERENCES inventory_items(id),
    FOREIGN KEY (from_storage_level_id)     REFERENCES storage_levels(id),
    FOREIGN KEY (to_storage_level_id)       REFERENCES storage_levels(id),
    FOREIGN KEY (created_by)                REFERENCES users(id),
    INDEX (inventory_item_id),
    INDEX (from_storage_level_id),
    INDEX (to_storage_level_id),
    INDEX (created_by)
);
