package com.weatherwhere.airservice.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "air_pollution_daily", schema = "air")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Data
public class RealTimeAirEntity extends BaseEntity {
    @Id
    @Column(name = "measuring_station")
    private String stationName;

    @Column(name = "data_time")
    private String dataTime;

    @Column(name = "so2_grade")
    private int so2Grade;

    @Column(name = "khai_value")
    private int khaiValue;

    @Column(name = "so2_value")
    private double so2Value;

    @Column(name = "co_value")
    private double coValue;

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
    private double no2Value;

    @Column(name = "pm10_grade")
    private int pm10Grade;

    @Column(name = "o3_value")
    private double o3Value;
}