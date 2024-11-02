package com.capstone3.GroupAppoint.appointment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentListParticipantDto {
    private Integer accountId;
    private String profile;
}
