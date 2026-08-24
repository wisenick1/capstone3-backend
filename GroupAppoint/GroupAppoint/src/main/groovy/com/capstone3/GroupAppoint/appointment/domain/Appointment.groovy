package com.capstone3.GroupAppoint.appointment.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

import java.time.LocalDateTime

@Entity
@Table(name = 'appointments')
class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    @Column(nullable = false)
    String title

    @Column(nullable = false)
    LocalDateTime scheduledAt

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AppointmentStatus status

    @Embedded
    AppointmentLocation location

    @OneToMany(mappedBy = 'appointment', cascade = CascadeType.ALL, orphanRemoval = true)
    List<AppointmentParticipant> participants = []

    protected Appointment() {
    }

    Appointment(String title, LocalDateTime scheduledAt, AppointmentStatus status,
                AppointmentLocation location) {
        this.title = title
        this.scheduledAt = scheduledAt
        this.status = status
        this.location = location
    }

    void addParticipant(AppointmentParticipant participant) {
        participants.add(participant)
        participant.appointment = this
    }
}
