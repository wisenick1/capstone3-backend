package com.capstone3.GroupAppoint.appointment.dto;

import lombok.*;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OngoingAppointmentDto extends BaseAppointmentDto{
    private Integer diffDay;
    private Integer participantCount;
    private Boolean isPhoto;
    private List<OngoingParticipantDto> participantList;
}
