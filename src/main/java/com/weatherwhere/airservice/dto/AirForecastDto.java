package com.weatherwhere.airservice.dto;

import com.weatherwhere.airservice.domain.AirForecast;

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
public class AirForecastDto {
    private String baseDate;
    private String city;
    private String forecast;
    private String reliability;

    // DTO -> Entity
    public AirForecast toEntity(){
        AirForecast airForecast= AirForecast.builder()
            .baseDate(baseDate)
            .city(city)
            .forecast(forecast)
            .reliability(reliability)
            .build();
        return airForecast;
    }


}
