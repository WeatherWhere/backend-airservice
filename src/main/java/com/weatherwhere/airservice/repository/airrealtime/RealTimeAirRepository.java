package com.weatherwhere.airservice.repository.airrealtime;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealTimeAirRepository extends JpaRepository<RealTimeAirEntity, String> {
}
