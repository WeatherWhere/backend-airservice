package com.weatherwhere.airservice.controller;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.airservice.dto.AirForecastDto;
import com.weatherwhere.airservice.service.AirForecastServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("air/forecast")
@Slf4j
public class AirForecastController {
    private final AirForecastServiceImpl airForecastService;

    public AirForecastController(AirForecastServiceImpl airForecastService){
        this.airForecastService=airForecastService;
    }

    @GetMapping(value = "/api")
    public List<AirForecastDto> getAirForecastApiData(@RequestBody JSONObject date) throws
        ParseException, URISyntaxException, UnsupportedEncodingException {
        System.out.println(date.get("date"));
        return airForecastService.getApiData(date);
    }
}
