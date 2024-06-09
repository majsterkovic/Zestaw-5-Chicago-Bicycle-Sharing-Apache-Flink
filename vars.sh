#!/bin/bash

export CLUSTER_NAME=$(/usr/share/google/get_metadata_value attributes/dataproc-cluster-name)
export BUCKET_NAME="pbd-2023-mh"
export INPUT_DIR="bicycle-input"
export INPUT_FILE="gs://pbd-2023-mh/project2/stations.csv"
export TRIPS_INPUT_TOPIC="trips-input"
export STATION_INPUT_TOPIC="station-input"
export BOOTSTRAP_SERVERS="$CLUSTER_NAME-w-0:9092"
export KAFKA_SLEEP_TIME=2
export KAFKA_GROUP_ID="bicycle-trips"
export ANOMALY_OUTPUT_TOPIC="anomaly-output"

ALL_IPS=$(hostname -I)
INTERNAL_IP=$(echo $ALL_IPS | awk '{print $1}')

export MYSQL_URL="jdbc:mysql://$INTERNAL_IP:6033/streamdb"
export MYSQL_USERNAME="streamuser"
export MYSQL_PASSWORD="stream"

export FLINK_CHECKPOINT_DIR="hdfs:///tmp/flink-checkpoints"
export HADOOP_CONF_DIR=/etc/hadoop/conf 
export HADOOP_CLASSPATH='hadoop classpath'