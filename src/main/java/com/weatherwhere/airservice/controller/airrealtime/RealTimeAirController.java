package com.weatherwhere.airservice.controller.airrealtime;

import java.text.ParseException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.service.airrealtime.RealTimeAirService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/air/realtime")
public class RealTimeAirController {
    private final RealTimeAirService realTimeAirService;

    //DB 업데이트
    @GetMapping("/api")
    public Object updateRealtimeAirDate() throws ParseException, org.json.simple.parser.ParseException {
        return realTimeAirService.updateRealtimeAirDate();
    }

    // 경도 x, y 받아서 가까운 측정소 검색 후 그 측정소의 정보를 DB에서 가져와 보여줌
    @GetMapping("/data")
    public ResultDTO<List<RealTimeAirEntity>> getRealTimeDBData(@RequestParam Double x, Double y) throws org.json.simple.parser.ParseException {
           ResultDTO<List<RealTimeAirEntity>> data = (ResultDTO<List<RealTimeAirEntity>>) realTimeAirService.getRealTimeDBData(x, y);
           return data;
    }
}
