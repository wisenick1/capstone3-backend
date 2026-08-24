package com.capstone3.GroupAppoint.appointment.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = 'appointment_participants')
class AppointmentParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    Long userId
    String profileImageUrl

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = 'appointment_id', nullable = false)
    Appointment appointment

    protected AppointmentParticipant() {
    }

    AppointmentParticipant(Long userId, String profileImageUrl) {
        this.userId = userId
        this.profileImageUrl = profileImageUrl
    }
}
