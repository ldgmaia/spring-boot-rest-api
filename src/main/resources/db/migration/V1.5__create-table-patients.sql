CREATE TABLE patients (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	email VARCHAR(100),
	phone VARCHAR(20) NOT NULL,
	active TINYINT NOT NULL DEFAULT 1,
	street VARCHAR(100) NOT NULL,
	city VARCHAR(100) NOT NULL,
	postal_code VARCHAR(6) NOT NULL,
	province CHAR(2) NOT NULL,

	PRIMARY KEY (id)
);
