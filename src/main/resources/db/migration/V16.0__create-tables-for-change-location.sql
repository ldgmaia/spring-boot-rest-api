CREATE TABLE IF NOT EXISTS location_user_groups (
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name                VARCHAR(255) NOT NULL,
    description         VARCHAR(255),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS location_user_group_users (
    id                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    group_id            BIGINT UNSIGNED NOT NULL,
    user_id             BIGINT UNSIGNED NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (group_id),
    INDEX (user_id),

    FOREIGN KEY (user_id)       REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id)      REFERENCES location_user_groups(id) ON DELETE CASCADE,
    UNIQUE (user_id, group_id)
);

CREATE TABLE IF NOT EXISTS location_user_group_permissions  (
    id                          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    location_user_groups_id     BIGINT UNSIGNED NOT NULL,
    from_location_area_id       BIGINT UNSIGNED NOT NULL,
    to_location_area_id         BIGINT UNSIGNED NOT NULL,
    created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),

    INDEX (location_user_groups_id),
    INDEX (from_location_area_id),
    INDEX (to_location_area_id),

    FOREIGN KEY (location_user_groups_id)       REFERENCES location_user_groups (id),
    FOREIGN KEY (from_location_area_id)         REFERENCES storage_areas (id),
    FOREIGN KEY (to_location_area_id)           REFERENCES storage_areas (id),
    UNIQUE (location_user_groups_id, from_location_area_id, to_location_area_id)
);
