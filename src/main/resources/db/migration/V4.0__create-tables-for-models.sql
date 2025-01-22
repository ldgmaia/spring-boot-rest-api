CREATE TABLE IF NOT EXISTS models (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    identifier VARCHAR(255),
    enabled BIT DEFAULT 1 NOT NULL,
    needs_mpn BIT,
    status VARCHAR(255),
    approved_by BIGINT UNSIGNED,
    categories_id BIGINT UNSIGNED,
    created_by BIGINT UNSIGNED NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,

    PRIMARY KEY (id),
    UNIQUE (name),
    INDEX (approved_by),
    INDEX (categories_id),
    INDEX (created_by),
    FOREIGN KEY (approved_by) REFERENCES users (id),
    FOREIGN KEY (created_by) REFERENCES users (id),
    FOREIGN KEY (categories_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS model_category_components (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    quantity INT UNSIGNED,
    category_components_id BIGINT UNSIGNED,
    models_id BIGINT UNSIGNED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (category_components_id),
    INDEX (models_id),
    FOREIGN KEY (category_components_id) REFERENCES category_components (id),
    FOREIGN KEY (models_id) REFERENCES models (id)
);

CREATE TABLE IF NOT EXISTS model_fields_values (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    fields_values_id BIGINT UNSIGNED,
    models_id BIGINT UNSIGNED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (fields_values_id, models_id),
    INDEX (fields_values_id),
    INDEX (models_id),
    FOREIGN KEY (fields_values_id) REFERENCES fields_values (id),
    FOREIGN KEY (models_id) REFERENCES models (id)
);
