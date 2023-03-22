package com.weatherwhere.airservice.domain;

import com.weatherwhere.airservice.dto.AirForecastDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="air_forecast", schema = "air")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AirForecastEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_forecast_id")
    private Long airForecastId;

    @Column(name="base_date")
    private String baseDate;
    @Column(name="city")
    private String city;

    @Column(name="forecast")
    private String forecast;
    @Column(name="reliability")
    private String reliability;

    public void update(AirForecastDto dto){
        this.baseDate= dto.getBaseDate();
        this.city= dto.getCity();
        this.forecast= dto.getForecast();
        this.reliability= dto.getReliability();
    }
}
