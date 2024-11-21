package com.capstone3.GroupAppoint.appointment.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentListDto {
    private Long diffHours;
    private Long diffMinutes;
    private Integer participantCount;
    private List<AppointmentListParticipantDto> participantList;
}
