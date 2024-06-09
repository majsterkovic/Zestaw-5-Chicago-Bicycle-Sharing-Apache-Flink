#!/bin/bash

# Tworzenie katalogu do przechowywania danych MySQL
mkdir -p /tmp/datadir

# Uruchomienie kontenera MySQL
docker run --name mymysql -v /tmp/datadir:/var/lib/mysql -p 6033:3306 \
           -e MYSQL_ROOT_PASSWORD=dockerpw -d mysql:debian

sleep 60

# Uruchomienie sesji bash w kontenerze
docker exec -it mymysql bash -c "

# Uruchomienie MySQL wewnątrz kontenera
mysql -uroot -pdockerpw <<EOF
CREATE USER 'streamuser'@'%' IDENTIFIED BY 'stream';
CREATE DATABASE IF NOT EXISTS streamdb CHARACTER SET utf8;
GRANT ALL ON streamdb.* TO 'streamuser'@'%';
EOF
"

# Uruchomienie MySQL jako użytkownik streamuser i utworzenie tabeli
docker exec -it mymysql bash -c "
mysql -ustreamuser -pstream streamdb <<EOF
CREATE TABLE aggsink (
    id INT,
    start INT,
    stop INT
);
EOF
"

docker exec -it mymysql bash -c "
mysql -ustreamuser -pstream streamdb -e \"
SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'aggsink';
\" > /tmp/table_exists.txt

if grep -q '1' /tmp/table_exists.txt; then
    echo 'Tabela sink istnieje.'
else
    echo 'Tabela sink nie istnieje.'
fi
"