package com.weatherwhere.airservice.service.airrealtime;

public interface GetTmXYAndStationService {
    // 경도 x, 위도 y로 측정소명 받기
    String getStationName(Double x, Double y);
}
