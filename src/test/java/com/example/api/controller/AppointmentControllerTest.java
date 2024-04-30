package com.example.api.controller;

import com.example.api.domain.appointments.AppointmentBookingService;
import com.example.api.domain.appointments.AppointmentCreateDTO;
import com.example.api.domain.appointments.AppointmentDetailsDTO;
import com.example.api.domain.doctors.Specialty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AppointmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<AppointmentCreateDTO> appointmentCreateJson;

    @Autowired
    private JacksonTester<AppointmentDetailsDTO> appointmentDetailsJson;

    @MockBean
    private AppointmentBookingService appointmentBookingService;

    @Test
    @DisplayName("It should return http code 400 when data is invalid")
    @WithMockUser
    void bookAppointmentScenario01() throws Exception {
        var response = mvc.perform(post("/appointments"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("It should return http code 200 when data is valid")
    @WithMockUser
    void bookAppointmentScenario02() throws Exception {
        var date = LocalDateTime.now().plusHours(2);
        var specialty = Specialty.CARDIOLOGY;

        var appointmentDetails = new AppointmentDetailsDTO(null, 2L, 5L, date);

        when(appointmentBookingService.booking(any())).thenReturn(appointmentDetails);

        var response = mvc
                .perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(appointmentCreateJson.write(
                                new AppointmentCreateDTO(2L, 5L, date, specialty)
                        ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var expectedJson = appointmentDetailsJson.write(
                appointmentDetails
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }
}
