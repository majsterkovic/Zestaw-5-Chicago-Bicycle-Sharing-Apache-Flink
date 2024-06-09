#!/bin/bash

echo "Producing data to Kafka"
java -cp /usr/lib/kafka/libs/*:KafkaProducer.jar com.bigdata.Producer \
    "$INPUT_DIR" "$KAFKA_SLEEP_TIME" "$TRIPS_INPUT_TOPIC" 1 "$BOOTSTRAP_SERVERS"