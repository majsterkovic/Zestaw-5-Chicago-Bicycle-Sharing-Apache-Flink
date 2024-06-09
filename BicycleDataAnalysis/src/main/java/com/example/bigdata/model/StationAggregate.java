package com.example.bigdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationAggregate {

    private int id;
    private int start;
    private int stop;

    public static StationAggregate fromTripStation(TripStation tripStation) {
        return new StationAggregate(
                tripStation.getStationId(),
                tripStation.isStartStop() ? 0 : 1,
                tripStation.isStartStop() ? 1 : 0
        );
    }
}
