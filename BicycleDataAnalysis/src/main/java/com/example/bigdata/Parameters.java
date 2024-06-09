package com.example.bigdata;

public interface Parameters {
    String BOOTSTRAP_SERVERS = "bootstrap-servers";
    String TRIP_INPUT_TOPIC = "trip-input-topic";
    String STATION_INPUT_FILE = "station-input-file";
    String KAFKA_GROUP_ID = "kafka-group-id";
    String MYSQL_URL = "mysql-url";
    String MYSQL_USERNAME = "mysql-username";
    String MYSQL_PASSWORD = "mysql-password";
    String FLINK_CHECKPOINT_DIR = "flink-checkpoint-dir";
    String ANOMALY_OUTPUT_TOPIC = "anomaly-output-topic";
}
