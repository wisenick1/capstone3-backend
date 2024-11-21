package com.capstone3.GroupAppoint.appointment.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;

    @Column(name = "title")
    private String title;

    @Column(name = "plan_date")
    private LocalDate planDate;

    @Column(name = "plan_time")
    private LocalTime planTime;

    // 위도
    @Column(name = "latitude")
    private Double latitude;

    // 경도
    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "location")
    private String location;

    // 주소
    @Column(name = "address")
    private String address;

    @ColumnDefault("0")
    private int state; // 약속 상태    0 : 약속 한참 전, 1 : 약속 한시간 전(GPS)공유, 2: 약속 끝

}
