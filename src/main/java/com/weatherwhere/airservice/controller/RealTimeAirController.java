package com.weatherwhere.airservice.controller;

import com.weatherwhere.airservice.dto.RealTimeAirDto;
import com.weatherwhere.airservice.service.RealTimeAirServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("air/realtime")
public class RealTimeAirController {
    private final RealTimeAirServiceImpl realTimeAirService;

    @GetMapping("/api")
    public Object updateRealtimeAirDate() throws ParseException, org.json.simple.parser.ParseException {
        return realTimeAirService.updateRealtimeAirDate();
    }
}
