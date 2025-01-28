CREATE TABLE IF NOT EXISTS admin_settings (
    id                          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    setting_key                 VARCHAR(255) NOT NULL UNIQUE,
    setting_value               TEXT NOT NULL,
    description                 VARCHAR(255),
    enabled                     BOOLEAN NOT NULL DEFAULT TRUE,
    prohibited_status           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY (setting_key)
);
