package com.example.api.domain.appointments.validations;

import com.example.api.domain.appointments.AppointmentCreateDTO;

public interface AppointmentBookingValidator {

    void validate(AppointmentCreateDTO data);
}
