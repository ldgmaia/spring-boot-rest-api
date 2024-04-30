CREATE TABLE IF NOT EXISTS users (
    id BIGINT unsigned NOT NULL AUTO_INCREMENT ,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    enabled bit DEFAULT 1 NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE uc_username (username),
    INDEX idx_username (username)
);

CREATE TABLE IF NOT EXISTS roles (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    primary key (id),
    unique uc_name (name),
    INDEX idx_name (name)
);

CREATE TABLE IF NOT EXISTS users_roles (
    id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	role_id BIGINT unsigned NOT NULL,
	user_id BIGINT unsigned NOT NULL,
	created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
	FOREIGN KEY (role_id) REFERENCES roles (id),
	FOREIGN KEY (user_id) REFERENCES users (id),
	unique uc_role_id_user_id (role_id, user_id),
	INDEX idx_role_id_user_id (role_id, user_id)
);

CREATE TABLE IF NOT EXISTS permissions (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	route VARCHAR(100) NOT NULL,
	description VARCHAR(100) NOT NULL,
	created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    unique uc_route (route),
    INDEX idx_route (route)
);

CREATE TABLE IF NOT EXISTS permissions_roles (
    id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	permission_id BIGINT unsigned NOT NULL,
	role_id BIGINT unsigned NOT NULL,
	created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
	FOREIGN KEY (permission_id) REFERENCES permissions (id),
	FOREIGN KEY (role_id) REFERENCES roles (id),
	unique uc_role_id_permission_id (role_id, permission_id),
	INDEX idx_role_id_permission_id (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS field_groups (
    id BIGINT unsigned NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    enabled bit DEFAULT 1 NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    unique uc_name (name),
    index idx_name (name)
);

CREATE TABLE IF NOT EXISTS fields (
    id BIGINT unsigned NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    data_type varchar(255) NOT NULL,
    field_type varchar(255) NOT NULL,
    field_groups_id BIGINT unsigned NOT NULL,
    is_multiple bit DEFAULT 0 NOT NULL,
    enabled bit DEFAULT 1 NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    PRIMARY KEY (id),
    UNIQUE uc_name (name),
    INDEX idx_field_groups_id (field_groups_id),
    FOREIGN KEY (field_groups_id) REFERENCES field_groups (id)
);

-- From here on up has been rewritten and updated

--CREATE TABLE IF NOT EXISTS `role` (
--  `role_id` BIGINT NOT NULL AUTO_INCREMENT,
--  `name` VARCHAR(255) NOT NULL,
--  `user_id` BIGINT unsigned,
--  PRIMARY KEY (`role_id`),
--  CONSTRAINT fk_role_user_unique_name FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
--);
--
--CREATE TABLE IF NOT EXISTS `functionality` (
--  `functionality_id` BIGINT NOT NULL AUTO_INCREMENT,
--  `name` VARCHAR(255) NOT NULL,
--  PRIMARY KEY (`functionality_id`),
--  UNIQUE (`name`)
--);
--
--CREATE TABLE IF NOT EXISTS `role_functionality_mapping` (
--  `role_functionality_mapping_id` BIGINT NOT NULL AUTO_INCREMENT,
--  `role_id` BIGINT NOT NULL,
--  `functionality_id` BIGINT NOT NULL,
--    `can_read` bit DEFAULT NULL,
--    `can_update` bit DEFAULT NULL,
--    `can_create` bit DEFAULT NULL,
--    `can_delete` bit DEFAULT NULL,
--  PRIMARY KEY (`role_functionality_mapping_id`),
--  CONSTRAINT fk_role_func_map_role FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`),
--  CONSTRAINT fk_role_func_map_func FOREIGN KEY (`functionality_id`) REFERENCES `functionality` (`functionality_id`)
--);
--
--CREATE TABLE IF NOT EXISTS `qr_data_entity` (
--  `qr_data_id` BIGINT NOT NULL AUTO_INCREMENT,
--  `qr_content` MEDIUMBLOB NOT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  `qr_text` VARCHAR(255) NOT NULL,
--  `status` VARCHAR(255) DEFAULT NULL,
--  `user_id` BIGINT unsigned NOT NULL,
--  PRIMARY KEY (`qr_data_id`),
--  UNIQUE (`user_id`),
--  INDEX (`user_id`),
--  CONSTRAINT fk_qr_data_entity_user FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `values_data` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `value_data` varchar(255) NOT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  UNIQUE (`value_data`)
--);
--
--CREATE TABLE IF NOT EXISTS `fields_values` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `values_data_id` bigint DEFAULT NULL,
--  `fields_id` bigint DEFAULT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  `score` DOUBLE DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`fields_id`),
--  INDEX (`values_data_id`),
--  CONSTRAINT `fk_fields_values_fields_id` FOREIGN KEY (`fields_id`) REFERENCES `fields` (`id`),
--  CONSTRAINT `fk_fields_values_values_data_id` FOREIGN KEY (`values_data_id`) REFERENCES `values_data` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `storage_zone` (
--  `zone_id` bigint NOT NULL AUTO_INCREMENT,
--  `status` varchar(255) DEFAULT NULL,
--  `zone_description` varchar(255) DEFAULT NULL,
--  `zone_name` varchar(255) NOT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`zone_id`),
--  UNIQUE (`zone_name`)
--);
--
--CREATE TABLE IF NOT EXISTS `storage_area` (
--  `area_id` bigint NOT NULL AUTO_INCREMENT,
--  `area_description` varchar(255) DEFAULT NULL,
--  `area_name` varchar(255) NOT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `storage_zone_id` bigint DEFAULT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`area_id`),
--  UNIQUE (`area_name`),
--  INDEX (`storage_zone_id`),
--  CONSTRAINT `fk_storage_area_storage_zone_id` FOREIGN KEY (`storage_zone_id`) REFERENCES `storage_zone` (`zone_id`)
--);
--
--CREATE TABLE IF NOT EXISTS `storage_location` (
--  `location_id` bigint NOT NULL AUTO_INCREMENT,
--  `location_description` varchar(255) DEFAULT NULL,
--  `location_name` varchar(255) NOT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `storage_area_id` bigint DEFAULT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`location_id`),
--  UNIQUE (`location_name`),
--  INDEX (`storage_area_id`),
--  CONSTRAINT `fk_storage_location_storage_area_id` FOREIGN KEY (`storage_area_id`) REFERENCES `storage_area` (`area_id`)
--);
--
--CREATE TABLE IF NOT EXISTS `storage_level` (
--  `level_id` bigint NOT NULL AUTO_INCREMENT,
--  `level_description` varchar(255) DEFAULT NULL,
--  `level_name` varchar(255) NOT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  `storage_location_id` bigint DEFAULT NULL,
--  PRIMARY KEY (`level_id`),
--  UNIQUE (`level_name`),
--  INDEX (`storage_location_id`),
--  CONSTRAINT `fk_storage_level_storage_location_id` FOREIGN KEY (`storage_location_id`) REFERENCES `storage_location` (`location_id`)
--);
--
--CREATE TABLE IF NOT EXISTS `category_groups` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  `name` varchar(255) NOT NULL,
--  PRIMARY KEY (`id`),
--  UNIQUE (`name`)
--);
--
--CREATE TABLE IF NOT EXISTS `categories` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `category_name` varchar(255) NOT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  `needs_post` bit DEFAULT NULL,
--  `needs_serial_number` bit DEFAULT NULL,
--  `category_group_id` bigint DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  UNIQUE (`category_name`),
--  INDEX (`category_group_id`),
--  CONSTRAINT `fk_categories_category_group_id` FOREIGN KEY (`category_group_id`) REFERENCES `category_groups` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `category_components` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  `child_category_id` bigint DEFAULT NULL,
--  `parent_category_id` bigint DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`child_category_id`),
--  INDEX (`parent_category_id`),
--  CONSTRAINT `fk_category_components_parent_category_id` FOREIGN KEY (`parent_category_id`) REFERENCES `categories` (`id`),
--  CONSTRAINT `fk_category_components_child_category_id` FOREIGN KEY (`child_category_id`) REFERENCES `categories` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `category_fields` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `data_level` varchar(255) NOT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  `category_id` bigint DEFAULT NULL,
--  `fields_id` bigint DEFAULT NULL,
--  `is_mandatory` bit DEFAULT NULL,
--  `order_on_label` BIGINT DEFAULT NULL,
--  `print_on_label` bit DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`category_id`),
--  INDEX (`fields_id`),
--  CONSTRAINT `fk_category_fields_fields_id` FOREIGN KEY (`fields_id`) REFERENCES `fields` (`id`),
--  CONSTRAINT `fk_category_fields_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `models` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `description` varchar(255) DEFAULT NULL,
--  `identifier` varchar(255) DEFAULT NULL,
--  `is_active` bit DEFAULT NULL,
--  `name` varchar(255) NOT NULL,
--  `needs_mpn` bit DEFAULT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `approved_by` bigint unsigned DEFAULT NULL,
--  `categories_id` bigint DEFAULT NULL,
--  `created_by` bigint unsigned DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  `deleted_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  UNIQUE (`name`),
--  INDEX (`approved_by`),
--  INDEX (`categories_id`),
--  INDEX (`created_by`),
--  CONSTRAINT `fk_models_approved_by` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`),
--  CONSTRAINT `fk_models_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
--  CONSTRAINT `fk_models_categories_id` FOREIGN KEY (`categories_id`) REFERENCES `categories` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `model_category_components` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `quantity` int DEFAULT NULL,
--  `category_components_id` bigint DEFAULT NULL,
--  `models_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`category_components_id`),
--  INDEX (`models_id`),
--  CONSTRAINT `fk_model_category_components_category_components_id` FOREIGN KEY (`category_components_id`) REFERENCES `category_components` (`id`),
--  CONSTRAINT `fk_model_category_components_models_id` FOREIGN KEY (`models_id`) REFERENCES `models` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `model_fields_values` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `field_values_id` bigint DEFAULT NULL,
--  `models_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`field_values_id`),
--  INDEX (`models_id`),
--  CONSTRAINT `fk_model_fields_values_field_values_id` FOREIGN KEY (`field_values_id`) REFERENCES `fields_values` (`id`),
--  CONSTRAINT `fk_model_fields_values_models_id` FOREIGN KEY (`models_id`) REFERENCES `models` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `manufacturer_part_number` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `description` varchar(255) DEFAULT NULL,
--  `is_active` bit DEFAULT NULL,
--  `mpn` varchar(255) NOT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `approved_by` bigint unsigned DEFAULT NULL,
--  `created_by` bigint unsigned DEFAULT NULL,
--  `models_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  `deleted_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  UNIQUE (`mpn`),
--  INDEX (`approved_by`),
--  INDEX (`created_by`),
--  INDEX (`models_id`),
--  CONSTRAINT `fk_manufacturer_part_number_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
--  CONSTRAINT `fk_manufacturer_part_number_approved_by` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`),
--  CONSTRAINT `fk_manufacturer_part_number_models_id` FOREIGN KEY (`models_id`) REFERENCES `models` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `manufacturer_part_numbers_field_values` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `field_value_id` bigint DEFAULT NULL,
--  `manufacture_part_number_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`field_value_id`),
--  INDEX (`manufacture_part_number_id`),
--  CONSTRAINT `fk_mpn_field_values_mpn_id` FOREIGN KEY (`manufacture_part_number_id`) REFERENCES `manufacturer_part_number` (`id`),
--  CONSTRAINT `fk_mpn_field_values_field_value_id` FOREIGN KEY (`field_value_id`) REFERENCES `fields_values` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `receivings` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `tracking_lading` varchar(255) DEFAULT NULL,
--  `carrier` varchar(255) DEFAULT NULL,
--  `type` varchar(255) DEFAULT NULL,
--  `supplier` varchar(255) DEFAULT NULL,
--  `identifier` bigint DEFAULT NULL,
--  `parcels` varchar(255) DEFAULT NULL,
--  `notes` varchar(255) DEFAULT NULL,
--  `is_po_fully_received` bit DEFAULT NULL,
--  `created_by` bigint unsigned DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`created_by`),
--  CONSTRAINT `fk_receivings_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `receiving_items` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `receiving_id` bigint DEFAULT NULL,
--  `quantity` bigint DEFAULT NULL,
--  `description` varchar(255) DEFAULT NULL,
--  `quantity_to_receive` bigint DEFAULT NULL,
--  `created_by` bigint unsigned DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`created_by`),
--  INDEX (`receiving_id`),
--  CONSTRAINT `fk_receiving_items_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
--  CONSTRAINT `fk_receiving_items_receiving_id` FOREIGN KEY (`receiving_id`) REFERENCES `receivings` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `receiving_pictures` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `receiving_id` bigint DEFAULT NULL,
--  `picture_content` MEDIUMBLOB NOT NULL,
--  `created_by` bigint unsigned DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`created_by`),
--  INDEX (`receiving_id`),
--  CONSTRAINT `fk_receiving_pictures_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
--  CONSTRAINT `fk_receiving_pictures_receiving_id` FOREIGN KEY (`receiving_id`) REFERENCES `receivings` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `section` (
--  `id` BIGINT NOT NULL AUTO_INCREMENT,
--  `name` VARCHAR(255) DEFAULT NULL,
--  `models_id` BIGINT DEFAULT NULL,
--  `section_order` BIGINT DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`models_id`),
--  CONSTRAINT `fk_section_models_id` FOREIGN KEY (`models_id`) REFERENCES `models` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `section_area` (
--  `id` BIGINT NOT NULL AUTO_INCREMENT,
--  `name` VARCHAR(255) DEFAULT NULL,
--  `sections_id` BIGINT DEFAULT NULL,
--  `area_order` BIGINT DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  `print_on_label` bit DEFAULT NULL,
--  `print_area_name_on_label` bit DEFAULT NULL,
--  `order_on_label` BIGINT DEFAULT NULL,
--  is_critical bit DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`sections_id`),
--  CONSTRAINT `fk_section_area_sections_id` FOREIGN KEY (`sections_id`) REFERENCES `section` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `section_areas_model` (
--  `id` BIGINT NOT NULL AUTO_INCREMENT,
--  `section_areas_id` BIGINT DEFAULT NULL,
--  `component_models_id` BIGINT DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`section_areas_id`),
--  INDEX (`component_models_id`),
--  CONSTRAINT `fk_section_areas_model_section_areas_id` FOREIGN KEY (`section_areas_id`) REFERENCES `section_area` (`id`),
--  CONSTRAINT `fk_section_areas_model_component_models_id` FOREIGN KEY (`component_models_id`) REFERENCES `models` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `models_category_fields` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `category_fields_id` bigint DEFAULT NULL,
--  `models_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`category_fields_id`),
--  INDEX (`models_id`),
--  CONSTRAINT `fk_models_category_fields_category_fields_id` FOREIGN KEY (`category_fields_id`) REFERENCES `category_fields` (`id`),
--  CONSTRAINT `fk_models_category_fields_models_id` FOREIGN KEY (`models_id`) REFERENCES `models` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `inventory_items` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `item_condition` varchar(255) DEFAULT NULL,
--  `receiving_item_id` bigint DEFAULT NULL,
--  `grade` varchar(255) DEFAULT NULL,
--  `location` varchar(255) DEFAULT NULL,
--  `post` varchar(255) DEFAULT NULL,
--  `main_rbid` varchar(255) DEFAULT NULL,
--  `serial_number` varchar(255) DEFAULT NULL,
--  `pulled` bit DEFAULT NULL,
--  `assessed_separately` bit DEFAULT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `manufacture_part_number_id` bigint DEFAULT NULL,
--  `section_area_id` bigint DEFAULT NULL,
--  `models_id` bigint DEFAULT NULL,
--  `parent_inventory_items_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  `last_inspected_by` bigint unsigned DEFAULT NULL,
--  `cosmetic_grade` varchar(255) DEFAULT NULL,
--  `functional_grade` varchar(255) DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`manufacture_part_number_id`),
--  INDEX (`section_area_id`),
--  INDEX (`models_id`),
--  INDEX (`parent_inventory_items_id`),
--  INDEX (`receiving_item_id`),
--  INDEX (last_inspected_by),
--  CONSTRAINT `fk_inventory_items_parent_inventory_items_id` FOREIGN KEY (`parent_inventory_items_id`) REFERENCES `inventory_items` (`id`),
--  CONSTRAINT `fk_inventory_items_models_id` FOREIGN KEY (`models_id`) REFERENCES `models` (`id`),
--  CONSTRAINT `fk_inventory_items_section_area_id` FOREIGN KEY (`section_area_id`) REFERENCES `section_area` (`id`),
--  CONSTRAINT `fk_inventory_items_receiving_item_id` FOREIGN KEY (`receiving_item_id`) REFERENCES `receiving_items` (`id`),
--  CONSTRAINT `fk_inventory_items_manufacture_part_number_id` FOREIGN KEY (`manufacture_part_number_id`) REFERENCES `manufacturer_part_number` (`id`),
--  CONSTRAINT `fk_last_inspected_by_inventory_item` FOREIGN KEY (`last_inspected_by`) REFERENCES `users` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `inventory_items_fields_values` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `field_values_id` bigint DEFAULT NULL,
--  `fields_id` bigint DEFAULT NULL,
--  `inventory_items_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`field_values_id`),
--  INDEX (`fields_id`),
--  INDEX (`inventory_items_id`),
--  CONSTRAINT `fk_inventory_items_fields_values_fields_id` FOREIGN KEY (`fields_id`) REFERENCES `fields` (`id`),
--  CONSTRAINT `fk_inventory_items_fields_values_field_values_id` FOREIGN KEY (`field_values_id`) REFERENCES `fields_values` (`id`),
--  CONSTRAINT `fk_inventory_items_fields_values_inventory_items_id` FOREIGN KEY (`inventory_items_id`) REFERENCES `inventory_items` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `inventory_items_components_models_mpn` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `categories_id` bigint DEFAULT NULL,
--  `inventory_items_id` bigint DEFAULT NULL,
--  `manufacture_part_number_id` bigint DEFAULT NULL,
--  `models_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`categories_id`),
--  INDEX (`manufacture_part_number_id`),
--  INDEX (`models_id`),
--  INDEX (`inventory_items_id`),
--  CONSTRAINT `fk_inventory_items_components_models_mpn_categories_id` FOREIGN KEY (`categories_id`) REFERENCES `categories` (`id`),
--  CONSTRAINT `fk_inventory_items_components_models_mpn_inventory_items_id` FOREIGN KEY (`inventory_items_id`) REFERENCES `inventory_items` (`id`),
--  CONSTRAINT `fk_inventory_items_components_models_mpn_mpn_id` FOREIGN KEY (`manufacture_part_number_id`) REFERENCES `manufacturer_part_number` (`id`),
--  CONSTRAINT `fk_inventory_items_components_models_mpn_models_id` FOREIGN KEY (`models_id`) REFERENCES `models` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `inventory_items_component_fields` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `category_fields_id` bigint DEFAULT NULL,
--  `field_values_id` bigint DEFAULT NULL,
--  `inventory_items_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`category_fields_id`),
--  INDEX (`field_values_id`),
--  INDEX (`inventory_items_id`),
--  CONSTRAINT `fk_inventory_items_component_fields_category_fields_id` FOREIGN KEY (`category_fields_id`) REFERENCES `category_fields` (`id`),
--  CONSTRAINT `fk_inventory_items_component_fields_field_values_id` FOREIGN KEY (`field_values_id`) REFERENCES `fields_values` (`id`),
--  CONSTRAINT `fk_inventory_items_component_fields_inventory_items_id` FOREIGN KEY (`inventory_items_id`) REFERENCES `inventory_items` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `marketplaces` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `name` varchar(255) DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `countries` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `country_code` bigint DEFAULT NULL,
--  `name` varchar(255) DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `provinces_states` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `name` varchar(255) DEFAULT NULL,
--  `countries_id` bigint NOT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`countries_id`),
--  CONSTRAINT `fk_provinces_states_countries1` FOREIGN KEY (`countries_id`) REFERENCES `countries` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `customers` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `customer_name` varchar(255) DEFAULT NULL,
--  `company_name` varchar(255) DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  `zip_code` varchar(255) DEFAULT NULL,
--  `address` varchar(255) DEFAULT NULL,
--  `city` varchar(255) DEFAULT NULL,
--  `phone_number` varchar(255) DEFAULT NULL,
--  `email` varchar(255) DEFAULT NULL,
--  `provinces_states_id` bigint NOT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (provinces_states_id),
--  CONSTRAINT fk_customer_provinces_states FOREIGN KEY (provinces_states_id) REFERENCES provinces_states(id)
--);
--
--CREATE TABLE IF NOT EXISTS `orders` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `order_number` varchar(255) DEFAULT NULL,
--  `notes` varchar(255) DEFAULT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `shipped_at` TIMESTAMP DEFAULT NULL,
--  `marketplaces_id` bigint DEFAULT NULL,
--  `customers_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`marketplaces_id`),
--  INDEX (`customers_id`),
--  CONSTRAINT `fk_orders_marketplaces1` FOREIGN KEY (`marketplaces_id`) REFERENCES `marketplaces` (`id`),
--  CONSTRAINT `fk_orders_customers1` FOREIGN KEY (`customers_id`) REFERENCES `customers` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `currencies` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `name` varchar(255) DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `order_items` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `name` varchar(255) DEFAULT NULL,
--  `description` varchar(255) DEFAULT NULL,
--  `unit_price` varchar(255) DEFAULT NULL,
--  `quantity` bigint NOT NULL,
--  `notes` varchar(255) DEFAULT NULL,
--  `orders_id` bigint NOT NULL,
--  `currencies_id` bigint NOT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`orders_id`),
--  INDEX (`currencies_id`),
--  CONSTRAINT `fk_order_items_orders1` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`),
--  CONSTRAINT `fk_order_items_currencies1` FOREIGN KEY (`currencies_id`) REFERENCES `currencies` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `order_items_inventory_items` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `order_items_id` bigint NOT NULL,
--  `inventory_items_id` bigint NOT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  `supervisor_qc` bit DEFAULT NULL,
--  `supervisor` varchar(255) DEFAULT NULL,
--  `item_notes` varchar(255) DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`inventory_items_id`),
--  INDEX (`order_items_id`),
--  CONSTRAINT `fk_order_items_inventory_items_order_items1` FOREIGN KEY (`order_items_id`) REFERENCES `order_items` (`id`),
--  CONSTRAINT `fk_order_items_inventory_items_inventory_items1` FOREIGN KEY (`inventory_items_id`) REFERENCES `inventory_items` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `asset_transfer_request` (
--  `asset_transfer_request_id` BIGINT NOT NULL AUTO_INCREMENT,
--  `status` VARCHAR(255) DEFAULT NULL,
--  `inventory_item_id` BIGINT DEFAULT NULL,
--  `initiator_user_id` BIGINT unsigned DEFAULT NULL,
--  `qr_data_id` BIGINT DEFAULT NULL,
--  `target_user_id` BIGINT unsigned DEFAULT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`asset_transfer_request_id`),
--  INDEX (`inventory_item_id`),
--  INDEX (`initiator_user_id`),
--  INDEX (`qr_data_id`),
--  INDEX (`target_user_id`),
--  CONSTRAINT `fk_target_user_id_asset_transfer_request` FOREIGN KEY (`target_user_id`) REFERENCES `users` (`id`),
--  CONSTRAINT `fk_initiator_user_id_asset_transfer_request` FOREIGN KEY (`initiator_user_id`) REFERENCES `users` (`id`),
--  CONSTRAINT `fk_inventory_item_id_asset_transfer_request` FOREIGN KEY (`inventory_item_id`) REFERENCES `inventory_items` (`id`),
--  CONSTRAINT `fk_qr_data_id_asset_transfer_request` FOREIGN KEY (`qr_data_id`) REFERENCES `qr_data_entity` (`qr_data_id`)
--);
--
--CREATE TABLE IF NOT EXISTS `asset_transfer_request_audit_log` (
--  `atr_audit_log_id` BIGINT NOT NULL AUTO_INCREMENT,
--  `message` VARCHAR(255) DEFAULT NULL,
--  `qr_data_id` bigint DEFAULT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`atr_audit_log_id`),
--  INDEX (`qr_data_id`),
--  CONSTRAINT `fk_qr_data_id_asset_transfer_request_audit_log` FOREIGN KEY (`qr_data_id`) REFERENCES `qr_data_entity` (`qr_data_id`)
--);
--
--CREATE TABLE IF NOT EXISTS `asset_transfer_request_data` (
--  `asset_transfer_request_data_id` BIGINT NOT NULL AUTO_INCREMENT,
--  `status` VARCHAR(255) DEFAULT NULL,
--  `inventory_item_id` BIGINT DEFAULT NULL,
--  `asset_transfer_request_id` bigint DEFAULT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`asset_transfer_request_data_id`),
--  INDEX (`inventory_item_id`),
--  INDEX (`asset_transfer_request_id`),
--  CONSTRAINT `fk_inventory_item_id_asset_transfer_request_data` FOREIGN KEY (`inventory_item_id`) REFERENCES `inventory_items` (`id`),
--  CONSTRAINT `fk_asset_transfer_request_id_asset_transfer_request_data` FOREIGN KEY (`asset_transfer_request_id`) REFERENCES `asset_transfer_request` (`asset_transfer_request_id`)
--);
--
--
--CREATE TABLE IF NOT EXISTS `location_history` (
--  `id` BIGINT NOT NULL AUTO_INCREMENT,
--  `location` VARCHAR(255) DEFAULT NULL,
--  `previous_location` VARCHAR(255) DEFAULT NULL,
--  `updated_by` VARCHAR(255) DEFAULT NULL,
--  `inventory_item_id` bigint DEFAULT NULL,
--  `creation_date` TIMESTAMP DEFAULT NULL,
--  `last_updated` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`inventory_item_id`),
--  CONSTRAINT `fk_inventory_item_id_location_history` FOREIGN KEY (`inventory_item_id`) REFERENCES `inventory_items` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `first_assessment` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `item_condition` varchar(255) DEFAULT NULL,
--  `receiving_item_id` bigint DEFAULT NULL,
--  `grade` varchar(255) DEFAULT NULL,
--  `location` varchar(255) DEFAULT NULL,
--  `post` varchar(255) DEFAULT NULL,
--  `main_rbid` varchar(255) DEFAULT NULL,
--  `serial_number` varchar(255) DEFAULT NULL,
--  `pulled` bit DEFAULT NULL,
--  `assessed_separately` bit DEFAULT NULL,
--  `cosmetic_grade` varchar(255) DEFAULT NULL,
--  `functional_grade` varchar(255) DEFAULT NULL,
--  `last_inspected_by` bigint unsigned DEFAULT NULL,
--  `status` varchar(255) DEFAULT NULL,
--  `manufacture_part_number_id` bigint DEFAULT NULL,
--  `section_area_name` varchar(255) DEFAULT NULL,
--  `models_id` bigint DEFAULT NULL,
--  `parent_inventory_items_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`manufacture_part_number_id`),
--  INDEX (`last_inspected_by`),
--  INDEX (`models_id`),
--  INDEX (`parent_inventory_items_id`),
--  INDEX (`receiving_item_id`),
--  CONSTRAINT `fk_parent_inventory_items_id_first_assessment` FOREIGN KEY (`parent_inventory_items_id`) REFERENCES `inventory_items` (`id`),
--  CONSTRAINT `fk_models_id_first_assessment` FOREIGN KEY (`models_id`) REFERENCES `models` (`id`),
--  CONSTRAINT `fk_receiving_item_id_first_assessment` FOREIGN KEY (`receiving_item_id`) REFERENCES `receiving_items` (`id`),
--  CONSTRAINT `fk_manufacture_part_number_id_first_assessment` FOREIGN KEY (`manufacture_part_number_id`) REFERENCES `manufacturer_part_number` (`id`),
--  CONSTRAINT `fk_last_inspected_by_first_assessment` FOREIGN KEY (`last_inspected_by`) REFERENCES `users` (`id`)
--);
--
--CREATE TABLE IF NOT EXISTS `first_assessment_fields_values` (
--  `id` bigint NOT NULL AUTO_INCREMENT,
--  `field_values_id` bigint DEFAULT NULL,
--  `first_assessment_id` bigint DEFAULT NULL,
--  `created_at` TIMESTAMP DEFAULT NULL,
--  `updated_at` TIMESTAMP DEFAULT NULL,
--  PRIMARY KEY (`id`),
--  INDEX (`field_values_id`),
--  INDEX (`first_assessment_id`),
--  CONSTRAINT `fk_field_values_id_first_assessment_fields_values` FOREIGN KEY (`field_values_id`) REFERENCES `fields_values` (`id`),
--  CONSTRAINT `fk_first_assessment_id_first_assessment_fields_values` FOREIGN KEY (`first_assessment_id`) REFERENCES `first_assessment` (`id`)
--);
