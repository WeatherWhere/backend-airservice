package com.weatherwhere.airservice.controller.airforecast;

import java.time.LocalDate;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDto;
import com.weatherwhere.airservice.dto.ResultDto;
import com.weatherwhere.airservice.service.airforecast.AirForecastApiService;
import com.weatherwhere.airservice.service.airforecast.GetAirForecastDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/air/forecast")
@Slf4j
@RequiredArgsConstructor
public class AirForecastController {
    private final AirForecastApiService airForecastService;

    private final GetAirForecastDataService getAirForecastDataService;
    // 공공데이터 api 호출해서 db에 저장
    @GetMapping(value = "/api")
    public ResultDto<Object> getAirForecastApiData(@RequestParam LocalDate date) throws
        ParseException, java.text.ParseException {
        return airForecastService.getApiData(date);
    }

    // 7일의 대기 주간예보 데이터 가져오기!
    @GetMapping(value = "/data")
    public ResultDto<Object> getSevenDaysAirForecastData(@ModelAttribute SearchAirForecastDto searchAirForecastDto) throws
        Exception {
        return getAirForecastDataService.getSevenDaysDataOfLocation(searchAirForecastDto);
    }
}
