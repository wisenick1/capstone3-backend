package com.capstone3.GroupAppoint.appointment

import com.capstone3.GroupAppoint.appointment.domain.Appointment
import com.capstone3.GroupAppoint.appointment.domain.AppointmentStatus
import com.capstone3.GroupAppoint.appointment.domain.Location
import com.capstone3.GroupAppoint.appointment.domain.Participant
import com.capstone3.GroupAppoint.appointment.repository.AppointmentRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

import java.time.LocalDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    MockMvc mockMvc

    @Autowired
    AppointmentRepository appointmentRepository

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll()

        // Intentionally inserted out of chronological order to verify sorting (AC4).
        def completed = new Appointment(
                "홍대 점심 약속",
                LocalDateTime.parse("2026-08-25T12:00:00"),
                AppointmentStatus.COMPLETED,
                new Location("홍대입구역", "서울특별시 마포구", 37.557, 126.924))
        completed.addParticipant(new Participant(3L, "https://example.com/profile3.jpg"))

        def upcomingLater = new Appointment(
                "강남역 저녁 약속",
                LocalDateTime.parse("2026-08-30T19:00:00"),
                AppointmentStatus.UPCOMING,
                new Location("강남역", "서울특별시 강남구", 37.498, 127.027))
        upcomingLater.addParticipant(new Participant(1L, "https://example.com/profile1.jpg"))
        upcomingLater.addParticipant(new Participant(2L, "https://example.com/profile2.jpg"))

        def upcomingEarlier = new Appointment(
                "서울숲 산책",
                LocalDateTime.parse("2026-08-28T10:00:00"),
                AppointmentStatus.UPCOMING,
                new Location("서울숲", "서울특별시 성동구", 37.544, 127.037))
        upcomingEarlier.addParticipant(new Participant(4L, "https://example.com/profile4.jpg"))

        appointmentRepository.saveAll([completed, upcomingLater, upcomingEarlier])
    }

    @Test
    void "AC1_AC2 - status가 없으면 모든 약속을 200으로 반환한다"() {
        mockMvc.perform(get("/api/v1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(3))
    }

    @Test
    void "AC3 - status가 주어지면 해당 상태의 약속만 반환한다"() {
        mockMvc.perform(get("/api/v1/appointments").param("status", "UPCOMING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.length()').value(2))
                .andExpect(jsonPath('$[0].status').value("UPCOMING"))
                .andExpect(jsonPath('$[1].status').value("UPCOMING"))
    }

    @Test
    void "AC4 - scheduledAt 기준 오름차순으로 정렬한다"() {
        mockMvc.perform(get("/api/v1/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].scheduledAt').value("2026-08-25T12:00:00"))
                .andExpect(jsonPath('$[1].scheduledAt').value("2026-08-28T10:00:00"))
                .andExpect(jsonPath('$[2].scheduledAt').value("2026-08-30T19:00:00"))
    }

    @Test
    void "AC5 - 존재하지 않는 status 값이면 400을 반환한다"() {
        mockMvc.perform(get("/api/v1/appointments").param("status", "INVALID_STATUS"))
                .andExpect(status().isBadRequest())
    }

    @Test
    void "Response 구조 - location과 participants, participantCount를 포함한다"() {
        mockMvc.perform(get("/api/v1/appointments").param("status", "UPCOMING"))
                .andExpect(status().isOk())
                // scheduledAt asc => 서울숲(1명) 먼저, 강남역(2명) 다음
                .andExpect(jsonPath('$[1].title').value("강남역 저녁 약속"))
                .andExpect(jsonPath('$[1].location.name').value("강남역"))
                .andExpect(jsonPath('$[1].location.latitude').value(37.498))
                .andExpect(jsonPath('$[1].participantCount').value(2))
                .andExpect(jsonPath('$[1].participants.length()').value(2))
                .andExpect(jsonPath('$[1].participants[0].userId').value(1))
                .andExpect(jsonPath('$[1].participants[0].profileImageUrl').value("https://example.com/profile1.jpg"))
    }
}
