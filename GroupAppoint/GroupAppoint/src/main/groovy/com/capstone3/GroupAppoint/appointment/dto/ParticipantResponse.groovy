package com.capstone3.GroupAppoint.appointment.dto

import com.capstone3.GroupAppoint.appointment.domain.Participant

class ParticipantResponse {

    Long userId
    String profileImageUrl

    ParticipantResponse(Long userId, String profileImageUrl) {
        this.userId = userId
        this.profileImageUrl = profileImageUrl
    }

    static ParticipantResponse from(Participant participant) {
        return new ParticipantResponse(
                participant.userId,
                participant.profileImageUrl
        )
    }
}
