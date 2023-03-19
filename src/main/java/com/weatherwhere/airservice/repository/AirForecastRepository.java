package com.weatherwhere.airservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.airservice.domain.AirForecast;

public interface AirForecastRepository extends JpaRepository<AirForecast,Long> {
    AirForecast findByBaseDateAndCity(String baseDate,String city);

}
