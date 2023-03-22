package com.weatherwhere.airservice.controller;

import com.weatherwhere.airservice.dto.RealTimeAirDto;
import com.weatherwhere.airservice.service.RealTimeAirService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RealTimeAirController {
    private final RealTimeAirService realTimeAirService;

    public RealTimeAirController(RealTimeAirService realTimeAirService) {
        this.realTimeAirService = realTimeAirService;
    }

    //open api에서 데이터를 받아오는 동시에 DB에 저장
    @GetMapping("/air/forecast/realtime")
    public RealTimeAirDto getAirQualityData(@RequestParam String stationName) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
                "?stationName=" + stationName +
                "&dataTerm=daily" +
                "&pageNo=1" +
                "&numOfRows=1" +
                "&returnType=json" +
                "&serviceKey="+System.getProperty("AIR_FORECAST_SERVICE_KEY_DE");
        RealTimeAirDto realTimeAirDto = restTemplate.getForObject(apiUrl, RealTimeAirDto.class);
        realTimeAirDto.setStationName(stationName);
        realTimeAirService.saveRealTimeAirData(realTimeAirDto); //Service 클래스에서 saveRealTimeAirData 메소드 불러옴
        return realTimeAirDto;
    }
}
