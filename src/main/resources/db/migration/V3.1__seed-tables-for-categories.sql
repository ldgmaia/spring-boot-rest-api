INSERT INTO category_groups
	(name,enabled)
VALUES
	('Parts and Accessories',1),
	('Computing Devices',1);

INSERT INTO categories
	(name,enabled,needs_post,needs_serial_number,category_group_id)
VALUES
	 ('Laptop',1,1,1,2),
	 ('Hard Drive',1,0,1,1),
	 ('RAM',1,0,1,1),
	 ('Keyboard Docking',1,0,1,1),
	 ('Tablet',1,1,1,2),
	 ('Battery',1,0,1,1),
	 ('Camera',1,0,0,1);

INSERT INTO category_components
	(child_category_id,parent_category_id,enabled)
	VALUES
	 (2,1,1),
	 (3,1,1),
	 (4,5,1),
	 (6,1,1),
	 (6,5,1),
	 (6,4,1),
	 (7,1,1),
	 (7,5,1);

INSERT INTO category_fields
	(data_level,category_id,fields_id,is_mandatory,order_on_label,print_on_label,enabled)
VALUES
	 ('MODEL',6,13,0,NULL,0,1),
	 ('MPN',6,14,0,NULL,0,1),
	 ('RBID',6,16,0,NULL,0,1),
	 ('FUNCTIONAL',6,2,0,NULL,0,1),
	 ('COSMETIC',6,8,0,NULL,0,1),
	 ('RBID',7,13,0,NULL,0,1),
	 ('FUNCTIONAL',2,2,0,NULL,0,1),
	 ('RBID',2,13,0,NULL,0,1),
	 ('MPN',2,16,0,NULL,0,1),
	 ('MODEL',2,14,0,NULL,0,1),
	 ('COSMETIC',4,8,0,NULL,0,1),
	 ('FUNCTIONAL',4,2,0,NULL,0,1),
	 ('MODEL',1,14,0,NULL,0,1),
	 ('MPN',1,13,0,NULL,0,1),
	 ('RBID',1,16,0,NULL,0,1),
	 ('FUNCTIONAL',1,2,0,NULL,0,1),
	 ('COSMETIC',1,8,0,NULL,0,1),
	 ('FUNCTIONAL',3,1,0,NULL,0,1),
	 ('MODEL',5,14,0,NULL,0,1),
	 ('MPN',5,13,0,NULL,0,1),
	 ('RBID',5,16,0,NULL,0,1),
	 ('FUNCTIONAL',5,2,0,NULL,0,1),
	 ('COSMETIC',5,8,0,NULL,0,1);
