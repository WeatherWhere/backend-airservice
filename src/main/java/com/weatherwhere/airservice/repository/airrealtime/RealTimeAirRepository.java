package com.weatherwhere.airservice.repository.airrealtime;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RealTimeAirRepository extends JpaRepository<RealTimeAirEntity, String> {
/*
    @Query("SELECT DISTINCT rta.stationName FROM RealTimeAirEntity rta")
    List<String> getStationNames();

 */

}
