CREATE TABLE IF NOT EXISTS models (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) DEFAULT NULL,
    identifier VARCHAR(255) DEFAULT NULL,
    enabled BIT DEFAULT 1 NOT NULL,
    needs_mpn BIT DEFAULT NULL,
    status VARCHAR(255) DEFAULT NULL,
    approved_by BIGINT UNSIGNED DEFAULT NULL,
    categories_id BIGINT UNSIGNED DEFAULT NULL,
    created_by BIGINT UNSIGNED DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT null,

    PRIMARY KEY (id),
    INDEX (approved_by),
    INDEX (categories_id),
    INDEX (created_by),
    FOREIGN KEY (approved_by) REFERENCES users (id),
    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (categories_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS model_category_components (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    quantity INT UNSIGNED DEFAULT NULL,
    category_components_id BIGINT UNSIGNED DEFAULT NULL,
    models_id BIGINT UNSIGNED DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (category_components_id),
    INDEX (models_id),
    FOREIGN KEY (category_components_id) REFERENCES category_components (id),
    FOREIGN KEY (models_id) REFERENCES models (id)
);

CREATE TABLE IF NOT EXISTS model_fields_values (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    fields_values_id BIGINT UNSIGNED DEFAULT NULL,
    models_id BIGINT UNSIGNED DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (fields_values_id),
    INDEX (models_id),
    FOREIGN KEY (fields_values_id) REFERENCES fields_values (id),
    FOREIGN KEY (models_id) REFERENCES models (id)
);
