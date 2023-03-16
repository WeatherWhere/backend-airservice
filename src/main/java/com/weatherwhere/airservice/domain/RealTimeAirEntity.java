package com.weatherwhere.airservice.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "air_pollution_daily")
@Getter
@Setter
@NoArgsConstructor
public class RealTimeAirEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "air_pollution_id")
    private Long id;

    @Column(name = "measuring_station")
    private String stationName;

    @Column(name = "data_time")
    private LocalDateTime dataTime;

    @Column(name = "so2_grade")
    private int so2Grade;

    @Column(name = "khai_value")
    private int khaiValue;

    @Column(name = "so2_value")
    private float so2Value;

    @Column(name = "co_value")
    private float coValue;

    @Column(name = "pm10_value")
    private int pm10Value;

    @Column(name = "o3_grade")
    private int o3Grade;

    @Column(name = "khai_grade")
    private int khaiGrade;

    @Column(name = "pm25_value")
    private int pm25Value;

    @Column(name = "no2_grade")
    private int no2Grade;

    @Column(name = "pm25_grade")
    private int pm25Grade;

    @Column(name = "co_grade")
    private int coGrade;

    @Column(name = "no2_value")
    private float no2Value;

    @Column(name = "pm10_grade")
    private int pm10Grade;

    @Column(name = "o3_value")
    private float o3Value;

    @Builder
    public RealTimeAirEntity(String stationName, LocalDateTime dataTime, int so2Grade, int khaiValue, float so2Value,
                          float coValue, int pm10Value, int o3Grade, int khaiGrade, int pm25Value, int no2Grade,
                          int pm25Grade, int coGrade, float no2Value, int pm10Grade, float o3Value) {
        this.stationName = stationName;
        this.dataTime = dataTime;
        this.so2Grade = so2Grade;
        this.khaiValue = khaiValue;
        this.so2Value = so2Value;
        this.coValue = coValue;
        this.pm10Value = pm10Value;
        this.o3Grade = o3Grade;
        this.khaiGrade = khaiGrade;
        this.pm25Value = pm25Value;
        this.no2Grade = no2Grade;
        this.pm25Grade = pm25Grade;
        this.coGrade = coGrade;
        this.no2Value = no2Value;
        this.pm10Grade = pm10Grade;
        this.o3Value = o3Value;
    }
}
