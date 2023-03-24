package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.weatherwhere.airservice.domain.AirForecastEntity;
import com.weatherwhere.airservice.dto.AirForecastDto;

public interface AirForecastApiService {
    // 대기 주간예보 api 데이터 받아오는 메서드
    List<AirForecastDto> getApiData(JSONObject date) throws ParseException, java.text.ParseException;

    // String 가공하여 Dto에 넣어주는 메서드
    List<AirForecastDto> dataToDto(String data, LocalDate date);

    // DTO -> Entity

    default AirForecastEntity toEntity(AirForecastDto dto){
        AirForecastEntity airForecastEntity = AirForecastEntity.builder()
            .baseDate(dto.getBaseDate())
            .city(dto.getCity())
            .forecast(dto.getForecast())
            .reliability(dto.getReliability())
            .build();
        return airForecastEntity;
    }

}

