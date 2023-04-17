package com.weatherwhere.airservice.dto.tour;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourAirRealTimeDataDTO {
    private String stationName;
    private LocalDateTime dataTime;
    private String pm10Grade;
}
