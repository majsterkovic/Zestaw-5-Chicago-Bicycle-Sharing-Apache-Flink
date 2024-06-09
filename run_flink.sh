#!/bin/bash
source vars.sh

flink run -m yarn-cluster -p 4  -yjm 2048m -ytm 2048m \
    -c com.example.bigdata.BicycleDataAnalysis BicycleDataAnalysis.jar \
    --bootstrap-servers "$BOOTSTRAP_SERVERS"\
    --kafka-group-id "$KAFKA_GROUP_ID" \
    --trip-input-topic "$TRIPS_INPUT_TOPIC" \
    --station-input-file "$INPUT_FILE" \
    --mysql-url "$MYSQL_URL" \
    --mysql-username "$MYSQL_USERNAME" \
    --mysql-password "$MYSQL_PASSWORD" \
    --anomaly-output-topic "$ANOMALY_OUTPUT_TOPIC" \
    --flink-checkpoint-dir "$FLINK_CHECKPOINT_DIR" \