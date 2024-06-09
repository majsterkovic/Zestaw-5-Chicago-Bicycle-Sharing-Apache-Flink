#!/bin/bash
source vars.sh

CHECKPOINT="$FLINK_CHECKPOINT_DIR/$1"
shift


flink run -m yarn-cluster -p 4  -yjm 1024m -ytm 1024m \
    -c com.example.bigdata.BicycleDataAnalysis \
    -s $CHECKPOINT BicycleDataAnalysis.jar \
    --bootstrap-servers "$BOOTSTRAP_SERVERS"\
    --kafka-group-id "$KAFKA_GROUP_ID" \
    --trip-input-topic "$TRIPS_INPUT_TOPIC" \
    --station-input-file "$INPUT_FILE" \
    --mysql-url "$MYSQL_URL" \
    --mysql-username "$MYSQL_USERNAME" \
    --mysql-password "$MYSQL_PASSWORD" \
    --anomaly-output-topic "$ANOMALY_OUTPUT_TOPIC" \
