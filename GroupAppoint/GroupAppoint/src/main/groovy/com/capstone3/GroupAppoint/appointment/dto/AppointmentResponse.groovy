package com.capstone3.GroupAppoint.appointment.dto

import com.capstone3.GroupAppoint.appointment.domain.Appointment
import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus

import java.time.LocalDateTime

class AppointmentResponse {

    Long id
    String title
    LocalDateTime scheduledAt
    AppointmentStatus status
    LocationResponse location
    int participantCount
    List<ParticipantResponse> participants

    AppointmentResponse(Long id, String title, LocalDateTime scheduledAt, AppointmentStatus status,
                        LocationResponse location, int participantCount, List<ParticipantResponse> participants) {
        this.id = id
        this.title = title
        this.scheduledAt = scheduledAt
        this.status = status
        this.location = location
        this.participantCount = participantCount
        this.participants = participants
    }

    static AppointmentResponse from(Appointment appointment) {
        List<ParticipantResponse> participants = appointment.participants
                .collect { ParticipantResponse.from(it) }

        return new AppointmentResponse(
                appointment.id,
                appointment.title,
                appointment.scheduledAt,
                appointment.status,
                LocationResponse.from(appointment.location),
                participants.size(),
                participants
        )
    }
}
