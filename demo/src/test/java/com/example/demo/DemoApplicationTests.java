package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    @Qualifier("bigdataApp")
    private JdbcTemplate bigdataApp;

    @Test
    public void contextLoads() {

        List<CityName> cityNames = bigdataApp.query("SELECT city_name FROM ads_ebike_order_hha WHERE city_id = 13 LIMIT 10", new BeanPropertyRowMapper(CityName.class));
        System.out.println(cityNames);
    }

}
