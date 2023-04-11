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
public class AirForecastDTO {
    private LocalDate baseDate;
    private String city;
    private String forecast;
    private String reliability;
}
