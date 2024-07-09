CREATE TABLE IF NOT EXISTS mpns (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    models_id BIGINT UNSIGNED,
    enabled BIT DEFAULT 1 NOT NULL,
    status VARCHAR(255),
    approved_by BIGINT UNSIGNED,
    created_by BIGINT UNSIGNED,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (approved_by),
    INDEX (created_by),
    INDEX (models_id),
    FOREIGN KEY (approved_by) REFERENCES users (id),
    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (models_id) REFERENCES models (id)
);

CREATE TABLE IF NOT EXISTS mpns_field_values (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    fields_values_id BIGINT UNSIGNED,
    mpns_id BIGINT UNSIGNED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE (fields_values_id, mpns_id),
    INDEX (fields_values_id),
    INDEX (mpns_id),
    FOREIGN KEY (fields_values_id) REFERENCES fields_values (id),
    FOREIGN KEY (mpns_id) REFERENCES mpns (id)
);
