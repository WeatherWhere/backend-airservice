package com.weatherwhere.airservice.service.airforecast;

import java.text.ParseException;
import java.util.List;

import com.weatherwhere.airservice.domain.AirForecastEntity;
import com.weatherwhere.airservice.dto.AirForecastDto;
import com.weatherwhere.airservice.dto.SearchAirForecastDto;

public interface GetAirForecastDataService {

    // 해당 위치 7일 대기오염 주간예보 DB 가져오기
    // startDate에서 필요한 형식은 yyyy-MM-dd
    List<AirForecastDto> getSevenDaysDataOfLocation(SearchAirForecastDto searchAirForecastDto) throws Exception;

    // Entity->DTO
    default AirForecastDto entityToDto(AirForecastEntity airForecastEntity){
        AirForecastDto airForecastDto=AirForecastDto.builder()
            .forecast(airForecastEntity.getForecast())
            .baseDate(airForecastEntity.getAirForecastId().getBaseDate())
            .city(airForecastEntity.getAirForecastId().getCity())
            .reliability(airForecastEntity.getReliability())
            .build();
        return airForecastDto;
    }
}
