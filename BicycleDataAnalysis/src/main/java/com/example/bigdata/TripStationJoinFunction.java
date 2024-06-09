package com.example.bigdata;

import com.example.bigdata.model.Trip;
import com.example.bigdata.model.Station;
import com.example.bigdata.model.TripStation;
import org.apache.flink.api.common.functions.JoinFunction;

public class TripStationJoinFunction implements JoinFunction<Trip, Station, TripStation> {
    @Override
    public TripStation join(Trip trip, Station station) {
        return new TripStation(
                trip.getTripId(),
                trip.isStartStop(),
                trip.getEventTime(),
                trip.getStationId(),
                trip.getTripDuration(),
                trip.getUserType(),
                trip.getGender(),
                trip.getWeek(),
                trip.getTemperature(),
                trip.getEvents(),
                station.getStationName(),
                station.getTotalDocks(),
                station.getDocksInService(),
                station.getStatus(),
                station.getLatitude(),
                station.getLongitude(),
                station.getLocation()
        );
    }
}