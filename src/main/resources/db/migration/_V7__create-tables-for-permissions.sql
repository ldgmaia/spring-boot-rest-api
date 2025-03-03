CREATE TABLE roles (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	name VARCHAR(100)  NOT NULL,

    UNIQUE (name),
	PRIMARY KEY (id)
);

CREATE TABLE permissions (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	route VARCHAR(100) NOT NULL,
	description VARCHAR(100) NOT NULL,
	role_id BIGINT unsigned NOT NULL,

	PRIMARY KEY (id),
	UNIQUE (route),
	CONSTRAINT fk_permissions_roles FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE users_permissions (
    id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	permission_id BIGINT unsigned NOT NULL,
	user_id BIGINT unsigned NOT NULL,

	PRIMARY KEY (id),
	CONSTRAINT fk_users_permissions_permissions_id FOREIGN KEY (permission_id) REFERENCES permissions (id),
	CONSTRAINT fk_users_permissions_user FOREIGN KEY (user_id) REFERENCES users (id)
);
