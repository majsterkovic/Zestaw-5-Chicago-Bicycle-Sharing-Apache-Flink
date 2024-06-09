package com.example.bigdata.connectors;

import com.example.bigdata.Parameters;
import com.example.bigdata.model.Trip;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SqlConnectorTrip {
        public static SinkFunction<Trip> getMySQLSink(ParameterTool properties) {
            JdbcStatementBuilder<Trip> statementBuilder = new JdbcStatementBuilder<Trip>() {
                @Override
                public void accept(PreparedStatement ps, Trip trip) throws SQLException {
                    ps.setInt(1, trip.getTripId());
                    ps.setBoolean(2, trip.isStartStop());
                    ps.setTimestamp(3, Timestamp.valueOf(trip.getEventTime()));
                    ps.setInt(4, trip.getStationId());
                    ps.setDouble(5, trip.getTripDuration());
                    ps.setString(6, trip.getUserType());
                    ps.setString(7, trip.getGender());
                    ps.setInt(8, trip.getWeek());
                    ps.setDouble(9, trip.getTemperature());
                    ps.setString(10, trip.getEvents());
                }
            };
        JdbcConnectionOptions connectionOptions = new
                JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
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

        SinkFunction<Trip> jdbcSink =
                JdbcSink.sink("insert into sink" +
                                "(tripId, startStop, eventTime, " +
                                "stationId, tripDuration, userType, gender, week, temperature, events) \n" +
                                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        statementBuilder,
                        executionOptions,
                        connectionOptions);
        return jdbcSink;

        /*
        create table sink (
            tripId integer,
            startStop boolean,
            eventTime datetime,
            stationId integer,
            tripDuration float,
            userType varchar(20),
            gender varchar(20),
            week integer,
            temperature float,
            events varchar(50));

         */
    }
}