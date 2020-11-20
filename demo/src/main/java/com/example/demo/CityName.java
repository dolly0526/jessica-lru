package com.example.demo;

/**
 * @author yusenyang
 * @create 2020/11/18 20:52
 */
public class CityName {
    private String cityName;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "CityName{" +
                "cityName='" + cityName + '\'' +
                '}';
    }
}
