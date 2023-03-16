package com.weatherwhere.airservice.repository;

import com.weatherwhere.airservice.domain.RealTimeAirEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealTimeAirRepository extends JpaRepository<RealTimeAirEntity, Long> {
}
