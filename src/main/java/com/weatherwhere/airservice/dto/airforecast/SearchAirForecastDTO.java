package com.weatherwhere.airservice.dto.airforecast;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 대기 주간 예보를 조회할 때 사용할 대기 주간 예보 복합키 DTO
 */
public class SearchAirForecastDTO {

    private LocalDate baseDate;
    private String city;

}
