insert into admin_settings (service, key_param, value_param)
values 
	('minimum_grading', 'cosmetic_critical', '0'),
	('minimum_grading', 'cosmetic_non_critical', '3'),
	('minimum_grading', 'functional_critical', '0'),
	('minimum_grading', 'functional_non_critical', '3'),
    ('QuickBooks', 'redirectUri', ' /api/v1/qbo/callback'),
	('QuickBooks', 'webhookUri', ' /api/v1/qbo/webhook'),
	('QuickBooks', 'webhookVerifier', ' '),
	('QuickBooks', 'refreshToken', ' '),
	('QuickBooks', 'accessToken', ' '),
	('QuickBooks', 'realmId', ' '),
	('QuickBooks', 'minorVersion', '75'),
	('QuickBooks', 'qboUrl', 'https://quickbooks.api.intuit.com'),
    ('QuickBooksSandbox', 'redirectUri', ' /api/v1/qbo/callback'),
	('QuickBooksSandbox', 'webhookUri', ' /api/v1/qbo/webhook'),
	('QuickBooksSandbox', 'webhookVerifier', ' '),
	('QuickBooksSandbox', 'refreshToken', ''),
	('QuickBooksSandbox', 'accessToken', ' '),
	('QuickBooksSandbox', 'realmId', ' '),
	('QuickBooksSandbox', 'minorVersion', '75'),
	('QuickBooksSandbox', 'qboUrl', 'https://sandbox-quickbooks.api.intuit.com');
	
insert into gradings (`type`, score, grade, company_grade)
values 
	('functional', 0, 'FA', 'FA'),
	('functional', 3, 'F3', null),
	('functional', 4, 'F4', null),
	('functional', 5, 'F5', null),
	('cosmetic', 1, 'C1', 'D*'),
	('cosmetic', 2, 'C2', 'D'),
	('cosmetic', 3, 'C3', 'C'),
	('cosmetic', 4, 'C4', 'B'),
	('cosmetic', 5, 'C5', 'B'),
	('cosmetic', 6, 'C6', 'A'),
	('cosmetic', 7, 'C7', 'A'),
	('cosmetic', 8, 'C8', 'A'),
	('cosmetic', 9, 'C9', 'OB');
