package com.example.bigdata.connectors;

import com.example.bigdata.model.Trip;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TripKafkaDeserializationSchema
        implements KafkaRecordDeserializationSchema<Trip> {

    private DateTimeFormatter formatter;

    @Override
    public void open(DeserializationSchema.InitializationContext context) {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<Trip> out) {
        Trip trip = new Trip();
        trip.setTripId(Integer.parseInt(new String(record.key())));

        String[] values = new String(record.value()).split(",");
        trip.setStartStop(Boolean.parseBoolean(values[1]));
        trip.setEventTime(LocalDateTime.parse(values[2], formatter));
        trip.setStationId(Integer.parseInt(values[3]));
        trip.setTripDuration(Double.parseDouble(values[4]));
        trip.setUserType(values[5]);
        trip.setGender(values[6]);
        trip.setWeek(Integer.parseInt(values[7]));
        trip.setTemperature(Double.parseDouble(values[8]));
        trip.setEvents(values[9]);
        out.collect(trip);
        }

    @Override
    public TypeInformation<Trip> getProducedType() {
        return TypeInformation.of(Trip.class);
    }
}
