package com.capstone3.GroupAppoint.appointment.web

import com.capstone3.GroupAppoint.appointment.domain.Appointment
import com.capstone3.GroupAppoint.appointment.domain.AppointmentLocation
import com.capstone3.GroupAppoint.appointment.domain.AppointmentParticipant
import com.capstone3.GroupAppoint.appointment.domain.AppointmentRepository
import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

import java.time.LocalDateTime

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTests {

    @Autowired
    MockMvc mockMvc

    @Autowired
    AppointmentRepository appointmentRepository

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll()

        appointmentRepository.save(appointment(
                '완료된 점심 약속', '2026-08-20T12:00:00', AppointmentStatus.COMPLETED, 1L))
        appointmentRepository.save(appointment(
                '강남역 저녁 약속', '2026-08-30T19:00:00', AppointmentStatus.UPCOMING, 1L, 2L))
        appointmentRepository.save(appointment(
                '진행 중인 모임', '2026-08-24T18:00:00', AppointmentStatus.IN_PROGRESS, 3L))
        appointmentRepository.save(appointment(
                '취소된 약속', '2026-09-01T10:00:00', AppointmentStatus.CANCELLED, 4L))
    }

    @Test
    void returnsAllAppointmentsSortedByScheduledAtWhenStatusIsAbsent() {
        mockMvc.perform(get('/api/v1/appointments'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(4)))
                .andExpect(jsonPath('$[0].title').value('완료된 점심 약속'))
                .andExpect(jsonPath('$[1].title').value('진행 중인 모임'))
                .andExpect(jsonPath('$[2].title').value('강남역 저녁 약속'))
                .andExpect(jsonPath('$[3].title').value('취소된 약속'))
    }

    @Test
    void returnsOnlyAppointmentsMatchingStatus() {
        mockMvc.perform(get('/api/v1/appointments').param('status', 'UPCOMING'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(1)))
                .andExpect(jsonPath('$[0].status').value('UPCOMING'))
                .andExpect(jsonPath('$[0].title').value('강남역 저녁 약속'))
    }

    @Test
    void returnsResponseDtoShapeIncludingLocationAndParticipants() {
        mockMvc.perform(get('/api/v1/appointments').param('status', 'UPCOMING'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].id').isNumber())
                .andExpect(jsonPath('$[0].scheduledAt').value('2026-08-30T19:00:00'))
                .andExpect(jsonPath('$[0].location.name').value('강남역'))
                .andExpect(jsonPath('$[0].location.address').value('서울특별시 강남구'))
                .andExpect(jsonPath('$[0].location.latitude').value(37.498d))
                .andExpect(jsonPath('$[0].location.longitude').value(127.027d))
                .andExpect(jsonPath('$[0].participantCount').value(2))
                .andExpect(jsonPath('$[0].participants', hasSize(2)))
                .andExpect(jsonPath('$[0].participants[0].userId').value(1))
                .andExpect(jsonPath('$[0].participants[0].profileImageUrl')
                        .value('https://example.com/profile1.jpg'))
    }

    @Test
    void returnsBadRequestForUnknownStatus() {
        mockMvc.perform(get('/api/v1/appointments').param('status', 'UNKNOWN'))
                .andExpect(status().isBadRequest())
    }

    private static Appointment appointment(String title, String scheduledAt,
                                           AppointmentStatus status, Long... userIds) {
        Appointment appointment = new Appointment(
                title,
                LocalDateTime.parse(scheduledAt),
                status,
                new AppointmentLocation('강남역', '서울특별시 강남구', 37.498d, 127.027d)
        )
        userIds.each { userId ->
            appointment.addParticipant(new AppointmentParticipant(
                    userId, "https://example.com/profile${userId}.jpg"))
        }
        appointment
    }
}
