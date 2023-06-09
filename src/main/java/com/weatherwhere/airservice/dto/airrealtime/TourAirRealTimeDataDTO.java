package com.weatherwhere.airservice.dto.airrealtime;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * Tour Service에 넘겨줄 데이터의 DTO
 */
public class TourAirRealTimeDataDTO {
    private String stationName;
    private LocalDateTime dataTime;
    private String pm10Grade;
    private int pm10Value;
    private int pm25Value;
    private String pm25Grade;
}
