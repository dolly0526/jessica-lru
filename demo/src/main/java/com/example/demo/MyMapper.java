package com.example.demo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yusenyang
 * @create 2020/11/18 20:50
 */
@Mapper
@Component
public interface MyMapper {

    List<CityName> selectCityName(CityId cityId);
}
