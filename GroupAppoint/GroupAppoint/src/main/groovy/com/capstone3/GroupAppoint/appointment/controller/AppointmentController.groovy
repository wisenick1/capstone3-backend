package com.capstone3.GroupAppoint.appointment.controller

import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus
import com.capstone3.GroupAppoint.appointment.dto.AppointmentResponse
import com.capstone3.GroupAppoint.appointment.service.AppointmentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/appointments")
class AppointmentController {

    private final AppointmentService appointmentService

    AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService
    }

    @GetMapping
    ResponseEntity<List<AppointmentResponse>> getAppointments(
            @RequestParam(name = "status", required = false) AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.getAppointments(status))
    }
}
