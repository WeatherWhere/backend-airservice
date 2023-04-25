package com.weatherwhere.airservice.service.airforecast;

import java.util.List;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;
import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDTO;
import com.weatherwhere.airservice.dto.ResultDTO;

public interface GetAirForecastDataService {

    /**
     * 해당 날짜와 지역에 해당하는 5일의 주간예보를 DB에서 조회하고 ResultDTO<List<AirForecastDTO>>를 리턴합니다.
     *
     * @param searchAirForecastDto DB에서 조회할 찾을 시작 날짜와 지역
     * @return DB에서 5일의 주간예보를 조회하는데 성공한다면 ResultDTO<List<AirForecastDTO>> 리턴
     */
    ResultDTO<List<AirForecastDTO>> getFiveDaysDataOfLocation(SearchAirForecastDTO searchAirForecastDto);

    /**
     * 대기 주간예보 Entity를 DTO로 변환하여 AirForecastDTO를 리턴합니다.
     *
     * @param airForecastEntity 변환할 AirForecastEntity
     * @return 대기 주간예보 Entity를 DTO로 변환한 AirForecastDTO를 리턴
     */
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
