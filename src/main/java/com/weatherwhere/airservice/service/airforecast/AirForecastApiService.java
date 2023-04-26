package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.util.List;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.domain.airforecast.AirForecastId;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;

public interface AirForecastApiService {
    /**
     * 해당 날짜로 api를 호출하여 데이터를 db에 업데이트하고 ResultDTO<List<AirForecastDTO>>를 리턴합니다.
     *
     * @param date 검색할 날짜
     * @return db에 업데이트를 성공했다면 ResultDTO<List<AirForecastDTO>>를 리턴, 실패했다면 예외처리
     */
    ResultDTO<List<AirForecastDTO>> getApiData(LocalDate date);


    /**
     * (대기 주간 예보)AirForecastDTO를 AirForecastEntity로 변환하여 AirForecastEntity를 리턴합니다.
     *
     * @param dto 대기 주간 예보 DTO
     * @return dto를 entity로 변환한 AirForecastEntity 리턴
     */
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


    /**
     * AirforecastEntity를 AirForecastDTO로 변환하여 AirForecastDTO를 리턴합니다.
     *
     * @param airForecastEntity 대기 주간 예보 Entity
     * @return Entity를 DTO로 변환한 AirForecastDTO 리턴
     */
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

