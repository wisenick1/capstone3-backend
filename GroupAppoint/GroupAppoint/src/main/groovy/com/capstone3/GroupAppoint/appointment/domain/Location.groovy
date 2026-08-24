package com.capstone3.GroupAppoint.appointment.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Location {

    @Column(name = "location_name")
    String name

    @Column(name = "location_address")
    String address

    Double latitude

    Double longitude

    Location() {}

    Location(String name, String address, Double latitude, Double longitude) {
        this.name = name
        this.address = address
        this.latitude = latitude
        this.longitude = longitude
    }
}
