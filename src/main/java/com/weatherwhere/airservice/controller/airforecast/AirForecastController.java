package com.weatherwhere.airservice.controller.airforecast;

import java.time.LocalDate;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;
import com.weatherwhere.airservice.service.ChangeAddrService;
import com.weatherwhere.airservice.service.airforecast.AirForecastApiService;
import com.weatherwhere.airservice.service.airforecast.GetAirForecastDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/air/forecast")
@Log4j2
@RequiredArgsConstructor
public class AirForecastController {
    private final AirForecastApiService airForecastService;

    private final ChangeAddrService changeAddrService;

    private final GetAirForecastDataService getAirForecastDataService;
    // 공공데이터 api 호출해서 db에 저장
    @GetMapping(value = "/api")
    public ResultDTO<List<AirForecastDTO>> getAirForecastApiData (@RequestParam LocalDate date) throws
        ParseException, java.text.ParseException {
        return airForecastService.getApiData(date);
    }


    // 5일의 대기 주간예보 데이터 주소와 베이스타임으로 가져오기!
    @GetMapping("/data")
    public ResultDTO<List<AirForecastDTO>> getFiveDaysData(@RequestParam String addr, String baseDate) throws Exception {
        return changeAddrService.getFiveDaysData(addr, baseDate);
    }
}

