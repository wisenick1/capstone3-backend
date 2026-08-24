package com.capstone3.GroupAppoint.appointment.domain

import jakarta.persistence.Embeddable

@Embeddable
class AppointmentLocation {

    String name
    String address
    Double latitude
    Double longitude

    protected AppointmentLocation() {
    }

    AppointmentLocation(String name, String address, Double latitude, Double longitude) {
        this.name = name
        this.address = address
        this.latitude = latitude
        this.longitude = longitude
    }
}
