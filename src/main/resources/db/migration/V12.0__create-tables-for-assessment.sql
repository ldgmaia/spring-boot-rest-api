CREATE TABLE IF NOT EXISTS assessments
(
    id                              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    section                         VARCHAR(255),
    area                            VARCHAR(255),
    present                         BIT DEFAULT 1,
    model                           VARCHAR(255),
    mpn                             VARCHAR(255),
    pulled                          BIT DEFAULT 0,
    status                          VARCHAR(255),
    post                            VARCHAR(255),
    company_grade                   VARCHAR(255),
    cosmetic_grade                  VARCHAR(255),
    functional_grade                VARCHAR(255),
    item_condition_id               BIGINT UNSIGNED,
    parent_inventory_item_id        BIGINT UNSIGNED,
    inventory_item_id               BIGINT UNSIGNED,
    receiving_items_id              BIGINT UNSIGNED,
    created_by                      BIGINT UNSIGNED NOT NULL,
    created_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (item_condition_id),
    INDEX (parent_inventory_item_id),
    INDEX (inventory_item_id),
    INDEX (receiving_items_id),
    INDEX (created_by),
    FOREIGN KEY (item_condition_id)         REFERENCES item_conditions (id),
    FOREIGN KEY (parent_inventory_item_id)  REFERENCES inventory_items (id),
    FOREIGN KEY (inventory_item_id)         REFERENCES inventory_items (id),
    FOREIGN KEY (receiving_items_id)        REFERENCES receiving_items (id),
    FOREIGN KEY (created_by)                REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS assessments_fields_values (
  id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  assessment_id   BIGINT UNSIGNED NOT NULL,
  field_values_id BIGINT UNSIGNED NOT NULL,
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

  PRIMARY KEY (id),
  INDEX (assessment_id),
  INDEX (field_values_id),
  FOREIGN KEY (assessment_id)   REFERENCES assessments (id),
  FOREIGN KEY (field_values_id) REFERENCES fields_values (id)
);
