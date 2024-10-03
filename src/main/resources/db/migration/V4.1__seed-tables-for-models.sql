INSERT INTO models
	(name,description,identifier,enabled,needs_mpn,status,approved_by,categories_id,created_by)
VALUES
	 ('laptop model','laptop model','laptop model',1,1,NULL,NULL,1,1),
	 ('Camera model','Camera model','Camera model',1,NULL,NULL,NULL,7,1),
	 ('hard drive model','hard drive model','hard drive model',1,0,NULL,NULL,2,1),
	 ('kb docking model','kb docking','kb docking',1,1,NULL,NULL,4,1),
	 ('battery model','battery model','battery model',1,NULL,NULL,NULL,6,1),
	 ('ram model','ram model','ram model',1,NULL,NULL,NULL,3,1),
	 ('tablet model','tablet model','tablet model',1,1,NULL,NULL,5,1);
INSERT INTO model_fields_values
    (fields_values_id,models_id)
VALUES
	(20,5),
	(18,3),
	(19,1),
	(18,7);
