package com.weatherwhere.airservice.service;


import com.weatherwhere.airservice.domain.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.RealTimeAirDto;
import com.weatherwhere.airservice.repository.RealTimeAirRepository;
import org.springframework.stereotype.Service;

@Service
public class RealTimeAirService {
    private final RealTimeAirRepository realTimeAirRepository;

    public RealTimeAirService(RealTimeAirRepository realTimeAirRepository) {
        this.realTimeAirRepository = realTimeAirRepository;
    }

    //받아온 open api 데이터를 저장
    public void saveRealTimeAirData(RealTimeAirDto realTimeAirDto) {
        RealTimeAirEntity realTimeAirEntity = RealTimeAirEntity.builder()
                .stationName(realTimeAirDto.getStationName())
                .dataTime(realTimeAirDto.getResponse().getBody().getItems().get(0).getDateTimeParsed())
                .so2Grade(realTimeAirDto.getResponse().getBody().getItems().get(0).getSo2Grade())
                .khaiValue(realTimeAirDto.getResponse().getBody().getItems().get(0).getKhaiValue())
                .so2Value(realTimeAirDto.getResponse().getBody().getItems().get(0).getSo2Value())
                .coValue(realTimeAirDto.getResponse().getBody().getItems().get(0).getCoValue())
                .pm10Value(realTimeAirDto.getResponse().getBody().getItems().get(0).getPm10Value())
                .o3Grade(realTimeAirDto.getResponse().getBody().getItems().get(0).getO3Grade())
                .khaiGrade(realTimeAirDto.getResponse().getBody().getItems().get(0).getKhaiGrade())
                .pm25Value(realTimeAirDto.getResponse().getBody().getItems().get(0).getPm25Value())
                .no2Grade(realTimeAirDto.getResponse().getBody().getItems().get(0).getNo2Grade())
                .pm25Grade(realTimeAirDto.getResponse().getBody().getItems().get(0).getPm25Grade())
                .coGrade(realTimeAirDto.getResponse().getBody().getItems().get(0).getCoGrade())
                .no2Value(realTimeAirDto.getResponse().getBody().getItems().get(0).getNo2Value())
                .pm10Grade(realTimeAirDto.getResponse().getBody().getItems().get(0).getPm10Grade())
                .o3Value(realTimeAirDto.getResponse().getBody().getItems().get(0).getO3Value())
                .build();

        realTimeAirRepository.save(realTimeAirEntity);
    }
}
