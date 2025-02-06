CREATE TABLE IF NOT EXISTS admin_settings (
    id                      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    service                 VARCHAR(255) NOT NULL,
    key_param               VARCHAR(1024) NOT NULL,
    value_param             VARCHAR(1024) NOT NULL,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS gradings (
    id                      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    type                    VARCHAR(255) NOT NULL,
    score                   DECIMAL(10, 2)  NOT NULL,
    grade                   VARCHAR(255) NOT NULL,
    company_grade           VARCHAR(255),
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id)
);
