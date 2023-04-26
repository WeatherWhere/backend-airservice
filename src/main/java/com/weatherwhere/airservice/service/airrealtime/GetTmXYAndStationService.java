package com.weatherwhere.airservice.service.airrealtime;

public interface GetTmXYAndStationService {
    /**
     * 경도 x, 위도 y로 측정소명을 리턴합니다.
     *
     * @param x 경도
     * @param y 위도
     * @return 경도와 위도로 찾은 측정소명을 리턴
     */
    String getStationName(Double x, Double y);
}
