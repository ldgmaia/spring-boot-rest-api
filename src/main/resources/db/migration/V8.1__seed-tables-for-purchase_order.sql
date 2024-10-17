INSERT INTO purchase_orders
	(status,po_number,currency,total,qbo_id,suppliers_id)
VALUES
	('Pending Receiving','3047','USD',51142.97,71095,1),
	('Pending Receiving','3083','CAD',7730,72076,2),
	('Pending Receiving','3089','USD',101250,72258,3),
	('Pending Receiving','3103','USD',20500,72607,4),
	('Pending Receiving','3111','USD',62461.13,72697,5);

INSERT INTO purchase_order_items
(name,description,quantity_ordered,unit_price,total,qbo_item_id,qbo_purchase_order_item_id,purchase_orders_id)
VALUES
	('Purchases','TOUGHBOOK CF-20',7,130.43,913.03,17,17,1),
	('Purchases','TOUGHBOOK CF-33/MK1/MARK 1',249,160.80,40039.78,17,17,1),
	('Purchases','TOUGHBOOK FZ-55',27,377.41,10190.16,17,17,1),
	('Purchases','CF-19',4,20,80,17,17,2),
	('Purchases','CF-54 MK1',3,75,225,17,17,2),
	('Purchases','CF-54 MK2',13,95,1235,17,17,2),
	('Purchases','CF-54 MK3',43,130,5590,17,17,2),
	('Purchases','FZ-55',2,300,600,17,17,2),
	('Purchases','FZ-40EAAAXBM',10,3050,30500,17,17,3),
	('Purchases','FZ-40EAAAFBM',10,3525,35250,17,17,3),
	('Purchases','FZ-40GCAAXBM',10,3550,35500,17,17,3),
	('Purchases','Panasonic CF-54',73,115.16,8406.68,17,17,4),
	('Purchases','Panaosnic CF-C2',32,115.16,3685.12,17,17,4),
	('Purchases','Panasonic FZ-55',63,115.16,7255.08,17,17,4),
	('Purchases','Panasonic FZ-G1',4,115.16,460.64,17,17,4),
	('Purchases','Panasonic FZ-M1',6,115.41,692.48,17,17,4),
	('Purchases','Panasonic TOUGHBOOK CF-20',7,121.53,850.71,17,17,5),
	('Purchases','Panasonic TOUGHBOOK CF-33/MK1',249,149.83,37307.67,17,17,5),
	('Purchases','Panasonic TOUGHBOOK CF-54',37,116.42,4307.54,17,17,5),
	('Purchases','Panasonic TOUGHBOOK FZ-55',27,351.65,9494.55,17,17,5),
	('Purchases','Panasonic TOUGHPAD FZ-G1',98,102.45,10040.1,17,17,5),
	('Freight','Freight',1,460.56,460.56,18,18,5);
