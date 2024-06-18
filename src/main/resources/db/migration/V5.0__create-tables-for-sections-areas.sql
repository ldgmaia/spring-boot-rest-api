CREATE TABLE IF NOT EXISTS sections (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    models_id BIGINT UNSIGNED DEFAULT NULL,
    section_order BIGINT UNSIGNED DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (models_id),
    CONSTRAINT fk_section_models_id FOREIGN KEY (models_id) REFERENCES models (id)
);

CREATE TABLE IF NOT EXISTS section_areas (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    sections_id BIGINT UNSIGNED DEFAULT NULL,
    area_order BIGINT UNSIGNED DEFAULT NULL,
    print_on_label BIT DEFAULT NULL,
    print_area_name_on_label BIT DEFAULT NULL,
    order_on_label BIGINT UNSIGNED DEFAULT NULL,
    is_critical BIT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    INDEX (sections_id),
    FOREIGN KEY (sections_id) REFERENCES sections (id)
);
