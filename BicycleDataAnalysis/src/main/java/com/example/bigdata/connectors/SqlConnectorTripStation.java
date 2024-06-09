package com.example.bigdata.connectors;

import com.example.bigdata.Parameters;
import com.example.bigdata.model.TripStation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SqlConnectorTripStation {
    public static SinkFunction<TripStation> getMySQLSink(ParameterTool properties) {
        JdbcStatementBuilder<TripStation> statementBuilder = new JdbcStatementBuilder<TripStation>() {
            @Override
            public void accept(PreparedStatement ps, TripStation tripStation) throws SQLException {
                ps.setInt(1, tripStation.getTripId());
                ps.setBoolean(2, tripStation.isStartStop());
                ps.setTimestamp(3, Timestamp.valueOf(tripStation.getEventTime()));
                ps.setInt(4, tripStation.getStationId());
                ps.setDouble(5, tripStation.getTripDuration());
                ps.setString(6, tripStation.getUserType());
                ps.setString(7, tripStation.getGender());
                ps.setInt(8, tripStation.getWeek());
                ps.setDouble(9, tripStation.getTemperature());
                ps.setString(10, tripStation.getEvents());
                ps.setString(11, tripStation.getStationName());
                ps.setInt(12, tripStation.getTotalDocks());
                ps.setInt(13, tripStation.getDocksInService());
                ps.setString(14, tripStation.getStatus());
                ps.setString(15, tripStation.getLatitude());
                ps.setString(16, tripStation.getLongitude());
                ps.setString(17, tripStation.getLocation());
            }
        };

        JdbcConnectionOptions connectionOptions = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl(properties.get(Parameters.MYSQL_URL))
                .withDriverName("com.mysql.jdbc.Driver")
                .withUsername(properties.get(Parameters.MYSQL_USERNAME))
                .withPassword(properties.get(Parameters.MYSQL_PASSWORD))
                .build();

        JdbcExecutionOptions executionOptions = JdbcExecutionOptions.builder()
                .withBatchSize(100)
                .withBatchIntervalMs(200)
                .withMaxRetries(5)
                .build();

        SinkFunction<TripStation> jdbcSink = JdbcSink.sink(
                "INSERT INTO sink (tripId, startStop, eventTime, stationId, tripDuration, userType, gender, week, temperature, events, stationName, totalDocks, docksInService, status, latitude, longitude, location) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                statementBuilder,
                executionOptions,
                connectionOptions
        );

        return jdbcSink;
    }

    /*
    CREATE TABLE sink (
        tripId INT,
        startStop BOOLEAN,
        eventTime DATETIME,
        stationId INT,
        tripDuration FLOAT,
        userType VARCHAR(20),
        gender VARCHAR(20),
        week INT,
        temperature FLOAT,
        events VARCHAR(50),
        stationName VARCHAR(50),
        totalDocks INT,
        docksInService INT,
        status VARCHAR(20),
        latitude VARCHAR(20),
        longitude VARCHAR(20),
        location VARCHAR(50)
    );
    */
}
