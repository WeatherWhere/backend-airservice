package com.weatherwhere.airservice.dto.airrealtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * DB에 측정소 명 기준으로 조회하거나 저장할 실시간 대기 정보 DTO
 */
public class RealTimeAirDTO {
    private String stationName;
    private LocalDateTime dataTime;
    private int so2Grade;
    private int khaiValue;
    private double so2Value;
    private double coValue;
    private int pm10Value;
    private int o3Grade;
    private int khaiGrade;
    private int pm25Value;
    private int no2Grade;
    private int pm25Grade;
    private int coGrade;
    private double no2Value;
    private int pm10Grade;
    private double o3Value;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

}
