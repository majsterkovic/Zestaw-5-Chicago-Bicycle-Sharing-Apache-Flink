package com.example.bigdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripStation implements Serializable {
    // Fields from Trip
    private int tripId;
    private boolean startStop;
    private LocalDateTime eventTime;
    private int stationId;
    private double tripDuration;
    private String userType;
    private String gender;
    private int week;
    private double temperature;
    private String events;

    // Fields from Station
    private String stationName;
    private int totalDocks;
    private int docksInService;
    private String status;
    private String latitude;
    private String longitude;
    private String location;
}
