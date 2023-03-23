package com.weatherwhere.airservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.airservice.domain.AirForecastEntity;

public interface AirForecastRepository extends JpaRepository<AirForecastEntity,Long> {
    AirForecastEntity findByBaseDateAndCity(String baseDate,String city);

}
