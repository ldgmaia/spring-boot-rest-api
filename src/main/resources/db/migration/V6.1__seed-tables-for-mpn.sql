INSERT INTO mpns
	(name,description,models_id,enabled,status,approved_by,created_by)
VALUES
	('mpn laptop',NULL,1,1,NULL,NULL,1),
	('mpn kb docking',NULL,4,1,NULL,NULL,1),
	('mpn tablet','mpn tablet',7,1,NULL,NULL,1);

INSERT INTO mpns_field_values
	(fields_values_id,mpns_id)
VALUES
	(21,3),
	(20,1);
