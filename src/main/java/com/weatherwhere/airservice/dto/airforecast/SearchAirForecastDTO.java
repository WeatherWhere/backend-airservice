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
public class SearchAirForecastDTO {

    private LocalDate baseDate;
    private String city;

}
