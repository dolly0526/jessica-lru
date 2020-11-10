package com.dolly.springboot.datademo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
public class DatademoApplicationTests {

    @Qualifier("dataSource")
    @Autowired
    private DataSource datasource;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() throws SQLException {
        System.out.println(datasource.getClass());
        System.out.println(datasource.getConnection());
    }

    @Test
    public void testRedisTemplate() {

        redisTemplate.opsForValue().set("name", "dolly");
        System.out.println(redisTemplate.opsForValue().get("name"));

    }
}
