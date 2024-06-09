#!/bin/bash

# Załaduj zmienne środowiskowe
source vars.sh

# Sprawdź, czy Maven jest zainstalowany, a jeśli nie, zainstaluj go
if ! mvn -version &>/dev/null; then
  echo "Installing Maven"
  sudo apt-get install maven -y
else
  echo "Maven is already installed"
fi

# Kompilacja projektu Flink
echo "Compiling Flink project"
mvn clean package -f ~/bigdata/BicycleDataAnalysis/pom.xml

# Sprawdzenie, czy plik JAR został utworzony
JAR_FILE=~/bigdata/BicycleDataAnalysis/target/BicycleDataAnalysis.jar
if [[ -f "$JAR_FILE" ]]; then
  echo "Copying jar to home directory"
  cp "$JAR_FILE" ~/bigdata
else
  echo "Error: JAR file not found. Compilation might have failed."
  exit 1
fi

echo "Done"
