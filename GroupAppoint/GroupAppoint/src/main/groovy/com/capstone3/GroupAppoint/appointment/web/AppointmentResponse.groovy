package com.capstone3.GroupAppoint.appointment.web

import com.capstone3.GroupAppoint.appointment.domain.Appointment
import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus

import java.time.LocalDateTime

record AppointmentResponse(
        Long id,
        String title,
        LocalDateTime scheduledAt,
        AppointmentStatus status,
        LocationResponse location,
        int participantCount,
        List<ParticipantResponse> participants
) {
    static AppointmentResponse from(Appointment appointment) {
        List<ParticipantResponse> participants = appointment.participants.collect {
            new ParticipantResponse(it.userId, it.profileImageUrl)
        }

        new AppointmentResponse(
                appointment.id,
                appointment.title,
                appointment.scheduledAt,
                appointment.status,
                new LocationResponse(
                        appointment.location.name,
                        appointment.location.address,
                        appointment.location.latitude,
                        appointment.location.longitude
                ),
                participants.size(),
                participants
        )
    }

    record LocationResponse(String name, String address, Double latitude, Double longitude) {
    }

    record ParticipantResponse(Long userId, String profileImageUrl) {
    }
}
