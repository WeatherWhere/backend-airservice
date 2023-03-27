package com.weatherwhere.airservice.dto;

import java.time.LocalDate;

import com.weatherwhere.airservice.domain.AirForecastId;

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
    private AirForecastId airForecastId;
    private String forecast;
    private String reliability;
}
