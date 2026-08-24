package com.capstone3.GroupAppoint.appointment.domain

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

    Long userId

    String profileImageUrl

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    Appointment appointment

    Participant() {}

    Participant(Long userId, String profileImageUrl) {
        this.userId = userId
        this.profileImageUrl = profileImageUrl
    }
}
