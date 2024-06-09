#!/bin/bash
source vars.sh

echo "Cleaning up"
rm -rf "$INPUT_DIR"
rm -rf "$INPUT_DIR.zip"

echo "Copying input files from GCS"
hadoop fs -copyToLocal gs://"${BUCKET_NAME}"/project2/trips.zip "$INPUT_DIR.zip" || exit

echo "Unzipping input files"
unzip -j "$INPUT_DIR.zip" -d "$INPUT_DIR" || exit

hadoop fs -rm -r -f "$FLINK_CHECKPOINT_DIR"

echo "Downloading dependencies"
wget https://repo1.maven.org/maven2/org/apache/flink/flink-connector-jdbc/1.15.4/flink-connector-jdbc-1.15.4.jar
wget https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
wget https://repo1.maven.org/maven2/org/apache/flink/flink-connector-kafka/1.15.4/flink-connector-kafka-1.15.4.jar
wget https://repo1.maven.org/maven2/org/apache/flink/flink-connector-cassandra_2.12/1.15.4/flink-connector-cassandra_2.12-1.15.4.jar
wget https://repo1.maven.org/maven2/org/apache/flink/flink-connector-kafka_2.12/1.14.6/flink-connector-kafka_2.12-1.14.6.jar
wget https://repo1.maven.org/maven2/org/apache/flink/flink-hadoop-fs/1.15.4/flink-hadoop-fs-1.15.4.jar

echo "Copying dependencies"
sudo cp flink-connector-jdbc-1.15.4.jar /usr/lib/flink/lib/
sudo cp mysql-connector-j-8.0.33.jar /usr/lib/flink/lib/
sudo cp flink-connector-kafka-1.15.4.jar /usr/lib/flink/lib/
sudo cp flink-connector-cassandra_2.12-1.15.4.jar /usr/lib/flink/lib/
sudo cp flink-connector-kafka_2.12-1.14.6.jar /usr/lib/flink/lib/
sudo cp flink-hadoop-fs-1.15.4.jar /usr/lib/flink/lib/


echo "Checking if kafka topics already exist"
kafka-topics.sh --delete --bootstrap-server "$BOOTSTRAP_SERVERS" --topic "$TRIPS_INPUT_TOPIC"

echo "Creating kafka topics"
kafka-topics.sh --create --topic "$TRIPS_INPUT_TOPIC" --bootstrap-server "$BOOTSTRAP_SERVERS" --replication-factor 1 --partitions 1

echo "Reset complete"