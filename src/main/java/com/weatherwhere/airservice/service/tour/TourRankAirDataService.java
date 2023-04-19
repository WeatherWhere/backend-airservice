package com.weatherwhere.airservice.service.tour;

import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.tour.TourAirRealTimeDataDTO;

public interface TourRankAirDataService {
    ResultDTO<TourAirRealTimeDataDTO> getRealTimeAirDBData(Double x, Double y);
}
