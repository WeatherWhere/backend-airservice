package com.weatherwhere.airservice.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.airservice.dto.AirForecastDto;
import com.weatherwhere.airservice.service.airforecast.AirForecastApiServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("air/forecast")
@Slf4j
@RequiredArgsConstructor
public class AirForecastController {
    private final AirForecastApiServiceImpl airForecastService;

    @GetMapping(value = "/api")
    public List<AirForecastDto> getAirForecastApiData(@RequestBody JSONObject date) throws
        ParseException, java.text.ParseException {
        System.out.println(date.get("date"));
        return airForecastService.getApiData(date);
    }
}
