CREATE TABLE IF NOT EXISTS sections (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    models_id BIGINT UNSIGNED,
    section_order BIGINT UNSIGNED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (models_id, name),
    INDEX (models_id),
    FOREIGN KEY (models_id) REFERENCES models (id)
);

CREATE TABLE IF NOT EXISTS section_areas (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    sections_id BIGINT UNSIGNED,
    area_order BIGINT UNSIGNED,
    print_on_label BIT,
    print_area_name_on_label BIT,
    order_on_label BIGINT UNSIGNED,
    is_critical BIT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (sections_id, name),
    INDEX (sections_id),
    FOREIGN KEY (sections_id) REFERENCES sections (id)
);
