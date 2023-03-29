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

    //DB 업데이트
    @GetMapping("/api")
    public Object updateRealtimeAirDate() throws ParseException, org.json.simple.parser.ParseException {
        return realTimeAirService.updateRealtimeAirDate();
    }

    // 경도 x, y 받아서 가까운 측정소 검색 후 그 측정소의 정보를 DB에서 가져와 보여줌R
    @GetMapping("/data")
    public ResponseEntity<RealTimeAirEntity> getRealTimeDBData(@RequestParam Double x, Double y) {
        try {
            RealTimeAirEntity data = realTimeAirService.getRealTimeDBData(x, y);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
