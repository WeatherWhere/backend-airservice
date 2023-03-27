package com.weatherwhere.airservice.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.airservice.dto.AirForecastDto;
import com.weatherwhere.airservice.dto.SearchAirForecastDto;
import com.weatherwhere.airservice.service.airforecast.AirForecastApiServiceImpl;
import com.weatherwhere.airservice.service.airforecast.GetAirForecastDataServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("air/forecast")
@Slf4j
@RequiredArgsConstructor
public class AirForecastController {
    private final AirForecastApiServiceImpl airForecastService;

    private final GetAirForecastDataServiceImpl getAirForecastDataService;
    // 공공데이터 api 호출해서 db에 저장
    @GetMapping(value = "/api")
    public List<AirForecastDto> getAirForecastApiData(@RequestBody JSONObject date) throws
        ParseException, java.text.ParseException {
        System.out.println(date.get("date"));
        return airForecastService.getApiData(date);
    }

    // 7일의 대기 주간예보 데이터 가져오기!
    @GetMapping(value = "/data")
    public List<AirForecastDto> getSevenDaysAirForecastData(@RequestBody SearchAirForecastDto searchAirForecastDto) throws
        Exception {
        return getAirForecastDataService.getSevenDaysDataOfLocation(searchAirForecastDto);
    }
}
