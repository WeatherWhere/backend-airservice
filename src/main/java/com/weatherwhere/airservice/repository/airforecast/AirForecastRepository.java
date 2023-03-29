package com.weatherwhere.airservice.repository.airforecast;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.domain.airforecast.AirForecastId;

public interface AirForecastRepository extends JpaRepository<AirForecastEntity, AirForecastId> {

   Optional<AirForecastEntity> findByAirForecastId(AirForecastId airForecastId);

}
