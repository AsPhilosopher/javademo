package com.plain.time_series.influx_db;

import org.apache.commons.collections4.CollectionUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FirstDemo {
    private InfluxDB influxDB;
    private String database = "mymonitor";

    @Before
    public void init() {
        influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086");
    }

    @Test
    public void insertAndQuery() {
        /**
         * 插入字符串双引号，查询单引号 ----坑
         * SELECT "<field_key>"::field,"<tag_key>"::tag
         */
        influxDB.write(database, "", InfluxDB.ConsistencyLevel.ANY,
                "invoke_monitor,isSuccess=true invokeTime=11,interfaceName=\"com.best.xingng.Demo\",methodName=\"demo\"");
        Query query = new Query("select isSuccess, interfaceName, methodName from invoke_monitor where " +
                "isSuccess = 'true' and interfaceName='com.best.xingng.Demo' and methodName='demo'", database);
        QueryResult queryResult = influxDB.query(query);
        List<QueryResult.Result> results = queryResult.getResults();
        System.out.println(results.size());
        for (QueryResult.Result result : results) {
            List<QueryResult.Series> seriess = result.getSeries();
            for (QueryResult.Series series : seriess) {
                System.out.println(series.getName());
                System.out.println(series.getColumns());
                System.out.println(series.getTags());
                System.out.println(series.getValues());
            }
        }
        query = new Query("select * from invoke_monitor", database);
        System.out.println(new InfluxDBResultMapper()
                .toPOJO(influxDB.query(query), PlainObject.class));

        /**
         * sum中的内容不能是tag，group by跟tag，才会分组聚合
         */
        query = new Query("select sum(invokeTime) from invoke_monitor group by isSuccess", database);
        List<QueryResult.Series> seriess = Optional.ofNullable(influxDB.query(query)).
                map(QueryResult::getResults).
                filter(CollectionUtils::isNotEmpty).
                map(_results -> _results.get(0).getSeries()).
                orElse(new ArrayList<>());
        System.out.println(seriess.get(0).getColumns());
        System.out.println(seriess.get(0).getTags());
        System.out.println(seriess.get(0).getValues());
        System.out.println(seriess.get(1).getColumns());
        System.out.println(seriess.get(1).getTags());
        System.out.println(seriess.get(1).getValues());
    }

    @After
    public void destroy() {
        influxDB.close();
    }
}
