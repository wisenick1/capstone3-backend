package com.capstone3.GroupAppoint.appointment.repository

import com.capstone3.GroupAppoint.appointment.domain.Appointment
import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus
import org.springframework.data.jpa.repository.JpaRepository

interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByOrderByScheduledAtAsc()

    List<Appointment> findByStatusOrderByScheduledAtAsc(AppointmentStatus status)
}
