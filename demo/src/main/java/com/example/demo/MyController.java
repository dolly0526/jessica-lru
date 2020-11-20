package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * @author yusenyang
 * @create 2020/11/18 20:49
 */
@RestController
public class MyController {

    @Autowired
    private MyMapper myMapper;

    @Autowired
    private DataSource dataSource;

    @PostMapping("/test")
    public List<CityName> getCityName(@RequestBody CityId cityId) throws SQLException {
        System.out.println(dataSource.getClass().getName());

        List<CityName> cityNames = myMapper.selectCityName(cityId);
        System.out.println(cityNames);
        return cityNames;
    }
}
