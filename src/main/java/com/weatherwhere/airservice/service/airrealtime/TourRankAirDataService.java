package com.weatherwhere.airservice.service.airrealtime;

import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airrealtime.TourAirRealTimeDataDTO;

public interface TourRankAirDataService {
    /**
     * 경도 x, 위도 y에 해당하는 대기 실시간 데이터를 DB에서 조회한 값을 투어에서 사용할  ResultDTO<TourAirRealTimeDataDTO>로 리턴합니다.
     *
     * @param x 조회할 경도
     * @param y 조회할 위도
     * @return DB에서 해당 대기 실시간 데이터를 조회했다면 ResultDTO<TourAirRealTimeDataDTO> 리턴
     */
    ResultDTO<TourAirRealTimeDataDTO> getRealTimeAirDBData(Double x, Double y);
}
