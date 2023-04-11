package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.domain.airforecast.AirForecastId;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;
import com.weatherwhere.airservice.dto.ResultDTO;

public interface AirForecastApiService {
    // 대기 주간예보 api 데이터 받아오는 메서드
    ResultDTO<List<AirForecastDTO>> getApiData(LocalDate date) throws ParseException, java.text.ParseException;

    // DTO -> Entity
    default AirForecastEntity toEntity(AirForecastDTO dto){
        AirForecastId airForecastId=AirForecastId.builder()
            .baseDate(dto.getBaseDate())
            .city(dto.getCity())
            .build();
        AirForecastEntity airForecastEntity = AirForecastEntity.builder()
            .airForecastId(airForecastId)
            .forecast(dto.getForecast())
            .reliability(dto.getReliability())
            .build();
        return airForecastEntity;
    }

    default AirForecastDTO toDto(AirForecastEntity airForecastEntity){
        AirForecastDTO airForecastDto= AirForecastDTO.builder()
            .forecast(airForecastEntity.getForecast())
            .baseDate(airForecastEntity.getAirForecastId().getBaseDate())
            .city(airForecastEntity.getAirForecastId().getCity())
            .reliability(airForecastEntity.getReliability())
            .build();
        return airForecastDto;
    }

}

