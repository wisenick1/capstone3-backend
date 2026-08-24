package com.capstone3.GroupAppoint.appointment.application

import com.capstone3.GroupAppoint.appointment.domain.Appointment
import com.capstone3.GroupAppoint.appointment.domain.AppointmentRepository
import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus
import com.capstone3.GroupAppoint.appointment.web.AppointmentResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AppointmentQueryService {

    private final AppointmentRepository appointmentRepository

    AppointmentQueryService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository
    }

    List<AppointmentResponse> findAppointments(AppointmentStatus status) {
        List<Appointment> appointments = status == null
                ? appointmentRepository.findAllByOrderByScheduledAtAsc()
                : appointmentRepository.findAllByStatusOrderByScheduledAtAsc(status)

        appointments.collect(AppointmentResponse::from)
    }
}
