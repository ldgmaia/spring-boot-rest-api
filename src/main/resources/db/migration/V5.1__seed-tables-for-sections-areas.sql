INSERT INTO sections
	(name,models_id,section_order)
VALUES
    ('section 1',1,1),
    ('section',2,1),
    ('section',3,1),
    ('section',4,1),
    ('section',5,1),
    ('section',6,1),
    ('section',7,1);

INSERT INTO section_areas
	(name,sections_id,area_order,print_on_label,print_area_name_on_label,order_on_label,is_critical)
VALUES
	('area 1',1,1,0,0,0,0),
	('area 2',1,2,0,0,0,0),
	('area 1',2,1,0,0,0,0),
	('area 2',2,2,0,0,0,0),
	('area 1',3,1,0,0,0,0),
	('area 2',3,2,0,0,0,0),
	('area 1',4,1,0,0,0,0),
	('area 2',4,2,0,0,0,0),
	('area 1',5,1,0,0,0,0),
	('area 2',5,2,0,0,0,0),
	('area 1',6,1,0,0,0,0),
	('area 2',6,2,0,0,0,0),
	('area 1',7,1,0,0,0,0),
	('area 2',7,2,0,0,0,0);