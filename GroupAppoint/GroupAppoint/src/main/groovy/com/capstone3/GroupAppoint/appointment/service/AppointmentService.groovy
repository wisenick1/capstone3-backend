package com.capstone3.GroupAppoint.appointment.service

import com.capstone3.GroupAppoint.appointment.domain.Appointment
import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus
import com.capstone3.GroupAppoint.appointment.dto.AppointmentResponse
import com.capstone3.GroupAppoint.appointment.repository.AppointmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AppointmentService {

    private final AppointmentRepository appointmentRepository

    AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository
    }

    @Transactional(readOnly = true)
    List<AppointmentResponse> getAppointments(AppointmentStatus status) {
        List<Appointment> appointments = (status == null)
                ? appointmentRepository.findAllByOrderByScheduledAtAsc()
                : appointmentRepository.findByStatusOrderByScheduledAtAsc(status)

        return appointments.collect { AppointmentResponse.from(it) }
    }
}
