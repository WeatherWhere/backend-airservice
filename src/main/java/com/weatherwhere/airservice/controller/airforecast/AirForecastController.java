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
import com.weatherwhere.airservice.service.airforecast.ChangeAddrService;
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

    // 공공데이터 api 호출해서 db에 저장

    /**
     * 공공데이터 주간예보 api를 호출하여 주간예보 데이터를 db에 업데이트하고 업데이트한 값을 ResultDTO<List<AirForecastDTO>>로 리턴합니다.
     *
     * @return db 업데이트를 성공했다면 ResultDTO<List<AirForecastDTO>>, 그렇지 않다면 예외처리고
     */
    @GetMapping(value = "/update")
    public ResultDTO<List<AirForecastDTO>> getAirForecastApiData () {
        // 대기 주간예보 업데이트: 오후 5시30분이므로 전날 데이터 저장
        LocalDate date = LocalDate.now().minusDays(1);
        return airForecastService.getApiData(date);
    }

    /**
     * 주어진 주소에 해당하고 주어진 날짜로부터 5일의 주간예보 데이터를 ResultDTO<List<AirForecastDTO>>로 리턴합니다.
     *
     * @param addr 찾을 주소
     * @param baseDate 찾을 날짜
     * @return 주소와 날짜에 해당하는 5일의 데이터를 조회했다면 ResultDTO<List<AirForecastDTO>>, 그렇지 않다면 예외처리
     * @throws Exception
     */
    @GetMapping("/data")
    public ResultDTO<List<AirForecastDTO>> getFiveDaysData(@RequestParam String addr, String baseDate) throws Exception {
        return changeAddrService.getFiveDaysData(addr, baseDate);
    }
}

