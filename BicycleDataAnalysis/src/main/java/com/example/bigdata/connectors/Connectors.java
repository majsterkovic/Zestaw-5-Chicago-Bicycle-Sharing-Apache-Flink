package com.example.bigdata.connectors;

import com.example.bigdata.Parameters;
import com.example.bigdata.model.Station;
import com.example.bigdata.model.Trip;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;


public class Connectors {
    public static KafkaSource<Trip> getTripsSource(ParameterTool properties) {
        return KafkaSource.<Trip>builder()
                .setBootstrapServers(properties.get(Parameters.BOOTSTRAP_SERVERS))
                .setTopics(properties.get(Parameters.TRIP_INPUT_TOPIC))
                .setGroupId(properties.get(Parameters.KAFKA_GROUP_ID))
                .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
                .setDeserializer(new TripKafkaDeserializationSchema())
                .build();
    }

    public static KafkaSink<String> getAnomalySink(ParameterTool properties) {
        return KafkaSink.<String>builder()
                .setBootstrapServers(properties.get(Parameters.BOOTSTRAP_SERVERS))
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(properties.get(Parameters.ANOMALY_OUTPUT_TOPIC))
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }
}
