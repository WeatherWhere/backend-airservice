package com.weatherwhere.airservice.controller.airrealtime;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.service.GetTmXYAndStationServiceImpl;
import com.weatherwhere.airservice.service.airrealtime.RealTimeAirServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("air/realtime")
public class RealTimeAirController {
    private final RealTimeAirServiceImpl realTimeAirService;
    private  final GetTmXYAndStationServiceImpl getTmXYAndStationService;

    @GetMapping("/api")
    public Object updateRealtimeAirDate() throws ParseException, org.json.simple.parser.ParseException {
        return realTimeAirService.updateRealtimeAirDate();
    }

    @GetMapping("/data")
    public ResponseEntity<RealTimeAirEntity> getRealTimeDBData(@RequestParam("stationName") String stationName) {
        try {
            RealTimeAirEntity data = realTimeAirService.getRealTimeDBData(stationName);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 경도 x, 위도 y로 측정소명 받기
    @GetMapping("/station")
    public String getStatinName(@RequestParam Double x, Double y) throws org.json.simple.parser.ParseException {
        return getTmXYAndStationService.getStationName(x,y);
    }
}
