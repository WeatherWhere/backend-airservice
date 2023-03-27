package com.weatherwhere.airservice.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.airservice.domain.AirForecastEntity;
import com.weatherwhere.airservice.domain.AirForecastId;

public interface AirForecastRepository extends JpaRepository<AirForecastEntity, AirForecastId> {
   Optional<AirForecastEntity> findByAirForecastId(AirForecastId airForecastId);
}
