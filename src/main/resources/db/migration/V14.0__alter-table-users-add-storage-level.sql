ALTER TABLE users
ADD COLUMN storage_level_id BIGINT UNSIGNED UNIQUE;

ALTER TABLE users
ADD FOREIGN KEY (storage_level_id)
REFERENCES storage_levels(id)
ON DELETE SET NULL
ON UPDATE CASCADE;

ALTER TABLE inventory_items
ADD COLUMN storage_level_id BIGINT UNSIGNED;

ALTER TABLE inventory_items
ADD FOREIGN KEY (storage_level_id)
REFERENCES storage_levels(id)
ON DELETE SET NULL
ON UPDATE SET NULL;


--ALTER TABLE users
--ADD CONSTRAINT fk_storage_level
--FOREIGN KEY (storage_level_id)
--REFERENCES storage_levels(id)
--ON DELETE CASCADE
--ON UPDATE CASCADE;
