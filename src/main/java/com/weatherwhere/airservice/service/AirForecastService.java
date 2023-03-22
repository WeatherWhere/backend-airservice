package com.weatherwhere.airservice.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import com.weatherwhere.airservice.domain.AirForecast;
import com.weatherwhere.airservice.dto.AirForecastDto;

public class AirForecastService {
    // 대기 주간예보 api 데이터 받아오는 메서드
    List<AirForecastDto> getApiData() throws ParseException, URISyntaxException {
        List<AirForecastDto> li=new ArrayList<>();
        return li;
    }

    // String 가공하여 Dto에 넣어주는 메서드
    List<AirForecastDto> dataToDto(String data, String date){
        List<AirForecastDto> li=new ArrayList<>();
        return li;
    }

    // DTO -> Entity
    AirForecast toEntity(AirForecastDto dto){
        AirForecast airForecast=new AirForecast();
        return airForecast;
    }
}

