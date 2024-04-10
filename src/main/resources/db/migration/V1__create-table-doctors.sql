CREATE TABLE doctors (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	email VARCHAR(100),
	cpso VARCHAR(10) NOT NULL,
	specialty VARCHAR(100) NOT NULL,
	street VARCHAR(100) NOT NULL,
	city VARCHAR(100) NOT NULL,
	postal_code VARCHAR(6) NOT NULL,
	province CHAR(2) NOT NULL,
	created_at datetime NOT NULL,
	updated_at datetime NOT NULL,

	PRIMARY KEY (id)
);
