docker exec -it mymysql bash -c "
mysql -ustreamuser -pstream streamdb -e \"
SELECT * FROM aggsink;
\" > /tmp/agg_sink_data.txt
"

docker cp mymysql:/tmp/agg_sink_data.txt /tmp/agg_sink_data.txt

cat /tmp/agg_sink_data.txt