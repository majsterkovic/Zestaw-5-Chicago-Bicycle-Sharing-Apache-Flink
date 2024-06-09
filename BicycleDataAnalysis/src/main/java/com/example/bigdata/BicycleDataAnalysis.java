package com.example.bigdata;

import com.example.bigdata.connectors.SqlConnectorAgg;
import com.example.bigdata.connectors.SqlConnectorTripStation;
import com.example.bigdata.model.StationAggregate;
import com.example.bigdata.model.TripStation;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import com.example.bigdata.connectors.Connectors;
import com.example.bigdata.model.Station;
import com.example.bigdata.model.Trip;

import java.time.Duration;
import java.time.ZoneOffset;


public class BicycleDataAnalysis {
    public static void main(String[] args) throws Exception {

        ParameterTool properties = ParameterTool.fromArgs(args);

        System.out.println("Properties");
        properties.toMap().forEach((k, v) -> System.out.println(k + ": " + v));

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 10000));
        env.enableCheckpointing(300000, CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setCheckpointTimeout(600000);

        if (properties.has(Parameters.FLINK_CHECKPOINT_DIR)) {
            env.getCheckpointConfig().setCheckpointStorage(properties.get(Parameters.FLINK_CHECKPOINT_DIR));
        }

        DataStream<Trip> tripsDS = env.fromSource(
                Connectors.getTripsSource(properties),
                WatermarkStrategy
                        .<Trip>forBoundedOutOfOrderness(Duration.ofHours(1))
                        .withTimestampAssigner((event, timestamp) -> event.getEventTime().toEpochSecond(ZoneOffset.UTC) * 1000),
                "Trips Source"
        );

        String path = properties.get(Parameters.STATION_INPUT_FILE);
        DataStream<Station> stations = env.readTextFile(path)
                .map(a -> a.split(","))
                .filter(a -> !a[0].equals("ID"))
                .map(a -> new Station(
                        Integer.parseInt(a[0]),
                        a[1],
                        Integer.parseInt(a[2]),
                        Integer.parseInt(a[3]),
                        a[4],
                        a[5],
                        a[6],
                        a[7] + " " + a[8]

                ));

        KeyedStream<Trip, Integer> keyedTrips = tripsDS.keyBy(Trip::getStationId);
        KeyedStream<Station, Integer> keyedStations = stations.keyBy(Station::getId);

        DataStream<TripStation> tripStationDS = keyedTrips
                .join(keyedStations)
                .where(Trip::getStationId)
                .equalTo(Station::getId)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .apply(new TripStationJoinFunction());

        //tripStationDS.addSink(SqlConnectorTripStation.getMySQLSink(properties));

        DataStream<StationAggregate> aggOutput = tripStationDS
                .map(StationAggregate::fromTripStation)
                .keyBy(StationAggregate::getId)
                .window(TumblingProcessingTimeWindows.of(Time.days(1)))
                .reduce((a, b) -> new StationAggregate(
                        a.getId(),
                        a.getStart() + b.getStart(),
                        a.getStop() + b.getStop()
                ));

        aggOutput.addSink(SqlConnectorAgg.getMySQLSink(properties));

        // wykrywanie anomalii


        env.execute("BicycleDataAnalysis");

    }
}
