package com.weatherwhere.airservice.dto.airforecast;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
/**
 * DB에 baseDate와 city를 기준으로 저장하거나 조회할 대기 주간 예보 DTO
 */
public class AirForecastDTO {
    private LocalDate baseDate;
    private String city;
    private String forecast;
    private String reliability;
}
