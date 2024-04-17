CREATE TABLE permission_groups (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) UNIQUE NOT NULL,

	PRIMARY KEY (id)
);

CREATE TABLE permissions (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) UNIQUE NOT NULL,
	description VARCHAR(100) NOT NULL,
	permission_group_id BIGINT unsigned NOT NULL,

	PRIMARY KEY (id),
	CONSTRAINT fk_permissions_permission_groups FOREIGN KEY (permission_group_id) REFERENCES permission_groups (id)
);

CREATE TABLE users_permissions (
    id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	permission_id BIGINT unsigned NOT NULL,
	user_id BIGINT unsigned NOT NULL,

	PRIMARY KEY (id),
	CONSTRAINT fk_users_permissions_permission_id FOREIGN KEY (permission_id) REFERENCES permissions (id),
	CONSTRAINT fk_users_permissions_user FOREIGN KEY (user_id) REFERENCES users (id)
);
