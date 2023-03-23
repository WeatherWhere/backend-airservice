package com.weatherwhere.airservice.controller;

import com.weatherwhere.airservice.dto.RealTimeAirDto;
import com.weatherwhere.airservice.service.RealTimeAirServiceImpl;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class RealTimeAirController {
    private final RealTimeAirServiceImpl realTimeAirService;

    @GetMapping("/air/forecast/realtime")
    public Object saveRealTimeAirData(@RequestParam String stationName) throws ParseException, java.text.ParseException {
        return realTimeAirService.saveRealTimeAirData(stationName);
    }
}
