package com.capstone3.GroupAppoint.appointment.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

import java.time.LocalDateTime

@Entity
class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    String title

    LocalDateTime scheduledAt

    @Enumerated(EnumType.STRING)
    AppointmentStatus status

    @Embedded
    Location location

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Participant> participants = []

    Appointment() {}

    Appointment(String title, LocalDateTime scheduledAt, AppointmentStatus status, Location location) {
        this.title = title
        this.scheduledAt = scheduledAt
        this.status = status
        this.location = location
    }

    void addParticipant(Participant participant) {
        participant.appointment = this
        this.participants.add(participant)
    }
}
