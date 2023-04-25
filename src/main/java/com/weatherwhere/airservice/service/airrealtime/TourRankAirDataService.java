package com.weatherwhere.airservice.service.airrealtime;

import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airrealtime.TourAirRealTimeDataDTO;

public interface TourRankAirDataService {
    ResultDTO<TourAirRealTimeDataDTO> getRealTimeAirDBData(Double x, Double y);
}
