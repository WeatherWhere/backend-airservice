package com.weatherwhere.airservice.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.airservice.domain.AirForecastEntity;

public interface AirForecastRepository extends JpaRepository<AirForecastEntity,Long> {
    AirForecastEntity findByBaseDateAndCity(LocalDate baseDate,String city);

}
