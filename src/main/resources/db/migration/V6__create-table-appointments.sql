CREATE TABLE appointments (
	id BIGINT unsigned NOT NULL AUTO_INCREMENT,
	doctor_id BIGINT unsigned NOT NULL,
	patient_id BIGINT unsigned NOT NULL,
	date datetime NOT NULL,

	PRIMARY KEY (id),
	constraint fk_appointments_doctors_id foreign key(doctor_id) references doctors(id),
	constraint fk_appointments_patients_id foreign key(patient_id) references patients(id)
);
