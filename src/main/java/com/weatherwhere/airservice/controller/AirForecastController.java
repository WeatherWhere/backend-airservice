package com.weatherwhere.airservice.controller;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import java.util.List;

import org.json.simple.parser.ParseException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.airservice.dto.AirForecastDto;
import com.weatherwhere.airservice.service.AirForecastService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/forecast")
@Slf4j
public class AirForecastController {
    private final AirForecastService airForecastService;

    public AirForecastController(AirForecastService airForecastService){
        this.airForecastService=airForecastService;
    }

    @GetMapping(value = "/api", produces = "application/json")
    public List<AirForecastDto> getAirForecastData() throws
        UnsupportedEncodingException, URISyntaxException, ParseException {
        return airForecastService.getData();
    }
}
