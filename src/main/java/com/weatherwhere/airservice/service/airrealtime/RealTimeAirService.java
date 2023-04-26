package com.weatherwhere.airservice.service.airrealtime;

import java.util.List;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.airrealtime.RealTimeAirDTO;
import com.weatherwhere.airservice.dto.airrealtime.StationNameDTO;
import jakarta.transaction.Transactional;

public interface RealTimeAirService {

    /**
     * 실시간 대기정보 Open API를 호출하여 데이터를 파싱하고 리턴하는 메서드
     *
     * @param stationName 측정소 명
     * @return 실시간 대기정보 result
     * @throws org.json.simple.parser.ParseException
     */
    Object getRealTimeAirData(String stationName) throws org.json.simple.parser.ParseException;

    /**
     * DB에 저장할 실시간 대기정보 데이터들을 List<RealTimeAirEntity>로 리턴,
     * IndexOutOfBoundsException이 발생하면 -1 값을 넣어 줌
     *
     * @param stationNameDtoList CSV 파일로부터 읽어온 측정소 명 리스트
     * @return List<RealTimeAirEntity> 리턴, 실패하면 예외처리
     */
    @Transactional
    List<RealTimeAirEntity> makeEntityList(List<StationNameDTO> stationNameDtoList);

    /**
     * 위경도 x, y의 값을 받아서 가까운 측정소 이름을 검색 후 그 측정소의 대기 정보를 DB에서 가져와서
     * ResultDTO<List<RealTimeAirEntity>> 로 리턴합니다
     *
     * @param x 경도
     * @param y 위도
     * @return 실시간 대기정보 ResultDTO<List<RealTimeAirEntity>>
     */
    Object getRealTimeDBData(Double x, Double y) throws org.json.simple.parser.ParseException;

    /**
     * RealTimeAirDTO를 RealTimeAirEntity로 변환하여 RealTimeAirEntity를 리턴합니다.
     *
     * @param dto RealTimeAirDTO
     * @return RealTimeAirEntity 리턴
     */
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

    /**
     * RealTimeEntity를 RealTimeDTO로 변환하여 RealTimeAirDTO를 리턴합니다.
     *
     * @param entity RealTimeAirEntity
     * @return RealTimeAirDTO
     */
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
