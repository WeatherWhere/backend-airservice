package com.weatherwhere.airservice.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weatherwhere.airservice.domain.AirForecastId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAirForecastDto {
    private LocalDate baseDate;
    private String city;
}
