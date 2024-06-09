package com.example.bigdata.connectors;



import com.example.bigdata.Parameters;
import com.example.bigdata.model.Station;
import com.example.bigdata.model.StationAggregate;
import com.example.bigdata.model.Trip;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SqlConnectorAgg {
    public static SinkFunction<StationAggregate> getMySQLSink(ParameterTool properties) {
        JdbcStatementBuilder<StationAggregate> statementBuilder = new JdbcStatementBuilder<StationAggregate>() {
            @Override
            public void accept(PreparedStatement ps, StationAggregate sa) throws SQLException {
                ps.setInt(1, sa.getId());
                ps.setInt(2, sa.getStart());
                ps.setInt(3, sa.getStop());
            }
        };
        JdbcConnectionOptions connectionOptions = new
                JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl(properties.get(Parameters.MYSQL_URL))
                .withDriverName("com.mysql.jdbc.Driver")
                .withUsername(properties.get(Parameters.MYSQL_USERNAME))
                .withPassword(properties.get(Parameters.MYSQL_PASSWORD))
                .build();

        JdbcExecutionOptions executionOptions = JdbcExecutionOptions.builder()
                .withBatchSize(100)
                .withBatchIntervalMs(200)
                .withMaxRetries(5)
                .build();

        SinkFunction<StationAggregate> jdbcSink =
                JdbcSink.sink("insert into aggsink)" +
                                "(id, start, stop" +
                                "values (?, ?, ?)",
                        statementBuilder,
                        executionOptions,
                        connectionOptions);
        return jdbcSink;

        /*
        create table aggsink (
            id integer,
            start id,
            stop id;
         */
    }
}