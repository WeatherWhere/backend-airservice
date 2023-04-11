package com.weatherwhere.airservice.service.airrealtime;

import java.text.ParseException;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.airrealtime.RealTimeAirDTO;

public interface RealTimeAirService {

    //실시간 대기 정보 가져오는 메서드
    Object getRealTimeAirData(String stationName) throws ParseException, org.json.simple.parser.ParseException;
    //실시간 대기 정보 DB에 저장하는 메서드
    Object saveRealTimeAirData(String stationName) throws ParseException, org.json.simple.parser.ParseException;
    //DB 업데이트 메서드
    Object updateRealtimeAirDate() throws ParseException, org.json.simple.parser.ParseException;
    //x, y 좌표 받아서 DB에서 해당하는 대기정보 보여주는 메서드
    Object getRealTimeDBData(Double x, Double y) throws org.json.simple.parser.ParseException;

    //Dto -> Entity 메서드
    default RealTimeAirEntity ToEntity(RealTimeAirDTO dto) {
        RealTimeAirEntity entity = RealTimeAirEntity.builder()
                .stationName(dto.getStationName())
                .dataTime(dto.getDataTime())
                .so2Grade(dto.getSo2Grade())
                .khaiValue(dto.getKhaiValue())
                .so2Value(dto.getSo2Value())
                .coValue(dto.getCoValue())
                .pm10Value(dto.getPm10Value())
                .o3Grade(dto.getO3Grade())
                .khaiGrade(dto.getKhaiGrade())
                .pm25Value(dto.getPm25Value())
                .no2Grade(dto.getNo2Grade())
                .pm25Grade(dto.getPm25Grade())
                .coGrade(dto.getCoGrade())
                .no2Value(dto.getNo2Value())
                .pm10Grade(dto.getPm10Grade())
                .o3Value(dto.getO3Value())
                .build();
        return entity;
    }

    //Entity -> Dto 메서드
    default RealTimeAirDTO ToDto(RealTimeAirEntity entity) {
        RealTimeAirDTO dto = RealTimeAirDTO.builder()
                .stationName(entity.getStationName())
                .dataTime(entity.getDataTime())
                .so2Grade(entity.getSo2Grade())
                .khaiValue(entity.getKhaiValue())
                .so2Value(entity.getSo2Value())
                .coValue(entity.getCoValue())
                .pm10Value(entity.getPm10Value())
                .o3Grade(entity.getO3Grade())
                .khaiGrade(entity.getKhaiGrade())
                .pm25Value(entity.getPm25Value())
                .no2Grade(entity.getNo2Grade())
                .pm25Grade(entity.getPm25Grade())
                .coGrade(entity.getCoGrade())
                .no2Value(entity.getNo2Value())
                .pm10Grade(entity.getPm10Grade())
                .o3Value(entity.getO3Value())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }
}
