CREATE TABLE IF NOT EXISTS category_groups (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    enabled BIT DEFAULT 1 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    enabled BIT DEFAULT 1 NOT NULL,
    needs_post BIT DEFAULT 0 NOT NULL,
    needs_serial_number BIT DEFAULT 0 NOT NULL,
    category_group_id BIGINT UNSIGNED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (name),
    INDEX (category_group_id),
    FOREIGN KEY (category_group_id) REFERENCES category_groups (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS category_components (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    child_category_id BIGINT UNSIGNED,
    parent_category_id BIGINT UNSIGNED,
    enabled BIT DEFAULT 1 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (child_category_id),
    INDEX (parent_category_id),
    UNIQUE uc_child_category_id_parent_category_id (child_category_id, parent_category_id),
    FOREIGN KEY (parent_category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (child_category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS category_fields (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    data_level VARCHAR(255) NOT NULL,
    category_id BIGINT UNSIGNED,
    fields_id BIGINT UNSIGNED,
    is_mandatory BIT,
    order_on_label BIGINT,
    print_on_label BIT,
    enabled BIT DEFAULT 1 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (category_id),
    INDEX (fields_id),
    FOREIGN KEY (fields_id) REFERENCES fields (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE
);
