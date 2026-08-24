package com.capstone3.GroupAppoint.appointment.domain

import org.springframework.data.jpa.repository.JpaRepository

interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByOrderByScheduledAtAsc()

    List<Appointment> findAllByStatusOrderByScheduledAtAsc(AppointmentStatus status)
}
