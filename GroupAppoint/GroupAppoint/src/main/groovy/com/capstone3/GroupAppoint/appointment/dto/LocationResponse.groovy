package com.capstone3.GroupAppoint.appointment.dto

import com.capstone3.GroupAppoint.appointment.domain.Location

class LocationResponse {

    String name
    String address
    Double latitude
    Double longitude

    LocationResponse(String name, String address, Double latitude, Double longitude) {
        this.name = name
        this.address = address
        this.latitude = latitude
        this.longitude = longitude
    }

    static LocationResponse from(Location location) {
        if (location == null) {
            return null
        }
        return new LocationResponse(
                location.name,
                location.address,
                location.latitude,
                location.longitude
        )
    }
}
