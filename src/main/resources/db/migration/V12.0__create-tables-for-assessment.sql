CREATE TABLE IF NOT EXISTS assessments
(
    id                              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    pulled                          BIT DEFAULT 0 NOT NULL,
    present                         BIT DEFAULT 1 NOT NULL,
    status                          VARCHAR(255) NOT NULL,
    company_grade                   VARCHAR(255),
    cosmetic_grade                  VARCHAR(255),
    functional_grade                VARCHAR(255),
    section_area_id                 BIGINT UNSIGNED NOT NULL,
    parent_inventory_item_id        BIGINT UNSIGNED NOT NULL,
    inventory_item_id               BIGINT UNSIGNED,
    created_by                      BIGINT UNSIGNED NOT NULL,
    receiving_items_id  BIGINT UNSIGNED NOT NULL,
    created_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at                      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    INDEX (section_area_id),
    INDEX (parent_inventory_item_id),
    INDEX (inventory_item_id),
    INDEX (created_by),
    INDEX (receiving_items_id),
    FOREIGN KEY (section_area_id)           REFERENCES section_areas (id),
    FOREIGN KEY (parent_inventory_item_id)  REFERENCES inventory_items (id),
    FOREIGN KEY (inventory_item_id)         REFERENCES inventory_items (id),
    FOREIGN KEY (created_by)                REFERENCES users (id),
    FOREIGN KEY (receiving_items_id)        REFERENCES receiving_items (id)
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
