package com.weatherwhere.airservice.service.airforecast;

import java.util.List;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;
import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDTO;
import com.weatherwhere.airservice.dto.ResultDTO;

public interface GetAirForecastDataService {

    // 해당 위치 5일 대기오염 주간예보 DB 가져오기
    // startDate에서 필요한 형식은 yyyy-MM-dd
    ResultDTO<List<AirForecastDTO>> getFiveDaysDataOfLocation(SearchAirForecastDTO searchAirForecastDto) throws Exception;

    /*
    // 하루 주간예보 가져오기
    ResultDTO<AirForecastDTO> getAirForecastOneDay(SearchAirForecastDTO searchAirForecastDto);
    */

    // Entity->DTO
    default AirForecastDTO entityToDto(AirForecastEntity airForecastEntity){
        AirForecastDTO airForecastDto= AirForecastDTO.builder()
            .forecast(airForecastEntity.getForecast())
            .baseDate(airForecastEntity.getAirForecastId().getBaseDate())
            .city(airForecastEntity.getAirForecastId().getCity())
            .reliability(airForecastEntity.getReliability())
            .build();
        return airForecastDto;
    }
}
