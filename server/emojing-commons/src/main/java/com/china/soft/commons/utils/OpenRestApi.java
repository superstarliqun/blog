package com.china.soft.commons.utils;

/**
 * @Description: 开放第三方接口
 * @author MingYue
 * @Date 2023/10/7
 */
public class OpenRestApi {


    /** 高德key address：https://lbs.amap.com/api/webservice/guide/api/weatherinfo#scene */
    private static String GaoDeKey = "42b477daae1f4bc4e5cc0462e8654b1e";

    /** 高德IP获取城市adcode值 */
    public static String GaoDeQueryAtCode = "https://restapi.amap.com/v3/ip?key=" + GaoDeKey + "&ip={param}";

    /** 高德获取近三天天气 */
    public static String GaoDeQueryWeatherInfo = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + GaoDeKey + "&city={param}&extensions=";


}
