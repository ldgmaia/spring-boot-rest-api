INSERT INTO purchase_orders
	(status,po_number,currency,total,qbo_id,suppliers_id)
VALUES
	('Open','3047','USD',51142.97,71095,1),
	('Open','3083','CAD',7730,72076,2),
	('Open','3089','USD',101250,72258,3),
	('Open','3103','USD',20500,72607,4),
	('Open','3111','USD',62461.13,72697,5);

INSERT INTO purchase_order_items
(name,quantity_ordered,unit_price,total,qbo_item_id,qbo_purchase_order_item_id,purchase_orders_id)
VALUES
	('Purchases',7,130.43,913.03,17,17,1),
	('Purchases',249,160.80,40039.78,17,17,1),
	('Purchases',27,377.41,10190.16,17,17,1),
	('Purchases',4,20,80,17,17,2),
	('Purchases',3,75,225,17,17,2),
	('Purchases',13,95,1235,17,17,2),
	('Purchases',43,130,5590,17,17,2),
	('Purchases',2,300,600,17,17,2),
	('Purchases',10,3050,30500,17,17,3),
	('Purchases',10,3525,35250,17,17,3),
	('Purchases',10,3550,35500,17,17,3),
	('Purchases',73,115.16,8406.68,17,17,4),
	('Purchases',32,115.16,3685.12,17,17,4),
	('Purchases',63,115.16,7255.08,17,17,4),
	('Purchases',4,115.16,460.64,17,17,4),
	('Purchases',6,115.4133333,692.48,17,17,4),
	('Purchases',7,121.53,850.71,17,17,5),
	('Purchases',249,149.83,37307.67,17,17,5),
	('Purchases',37,116.42,4307.54,17,17,5),
	('Purchases',27,351.65,9494.55,17,17,5),
	('Purchases',98,102.45,10040.1,17,17,5),
	('Freight',1,460.56,460.56,18,18,5);
