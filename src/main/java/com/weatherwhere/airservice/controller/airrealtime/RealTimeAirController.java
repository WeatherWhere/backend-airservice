package com.weatherwhere.airservice.controller.airrealtime;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airrealtime.TourAirRealTimeDataDTO;
import com.weatherwhere.airservice.service.airrealtime.RealTimeAirService;
import com.weatherwhere.airservice.service.airrealtime.TourRankAirDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@RestController
@RequiredArgsConstructor
@RequestMapping("/air/realtime")
@Log4j2
public class RealTimeAirController {
    private final RealTimeAirService realTimeAirService;
    private final TourRankAirDataService tourRankAirDataService;

    /**
     * 위경도 x, y의 값을 받아서 가까운 측정소 이름을 검색 후 그 측정소의 대기 정보를 DB에서 가져와서 ResultDTO<List<RealTimeAirEntity>> 로 리턴합니다.
     * @param x 위도
     * @param y 경도
     * @return 주어진 위경도 x, y에 해당하는 지역의 실시간 대기정보를 리턴
     * @throws org.json.simple.parser.ParseException
     */
    @GetMapping("/data")
    public ResultDTO<List<RealTimeAirEntity>> getRealTimeDBData(@RequestParam Double x, Double y) throws org.json.simple.parser.ParseException {
           ResultDTO<List<RealTimeAirEntity>> data = (ResultDTO<List<RealTimeAirEntity>>) realTimeAirService.getRealTimeDBData(x, y);
           return data;
    }

    // 관광에서 쓸 api
    // 위경도를 바탕으로 실시간 데이터 가져오기

    /**
     *
     * @param x
     * @param y
     * @return
     */
    @GetMapping(value = "/tour/data")
    public ResultDTO<TourAirRealTimeDataDTO> getAirForecastData(@RequestParam Double x, Double y){
        log.info("tour로 보낼 데이터 조회");
        return tourRankAirDataService.getRealTimeAirDBData(x, y);

    }

}
