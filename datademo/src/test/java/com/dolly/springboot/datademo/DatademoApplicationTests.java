package com.dolly.springboot.datademo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
public class DatademoApplicationTests {

    @Qualifier("dataSource")
    @Autowired
    private DataSource datasource;

    @Test
    public void contextLoads() throws SQLException {
        System.out.println(datasource.getClass());
        System.out.println(datasource.getConnection());
    }
}
