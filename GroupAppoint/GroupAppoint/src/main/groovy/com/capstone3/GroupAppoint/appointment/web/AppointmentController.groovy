package com.capstone3.GroupAppoint.appointment.web

import com.capstone3.GroupAppoint.appointment.application.AppointmentQueryService
import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/api/v1/appointments')
class AppointmentController {

    private final AppointmentQueryService appointmentQueryService

    AppointmentController(AppointmentQueryService appointmentQueryService) {
        this.appointmentQueryService = appointmentQueryService
    }

    @GetMapping
    ResponseEntity<List<AppointmentResponse>> getAppointments(
            @RequestParam(name = 'status', required = false) AppointmentStatus status) {
        ResponseEntity.ok(appointmentQueryService.findAppointments(status))
    }
}
