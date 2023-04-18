package com.weatherwhere.airservice.service.tour;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.tour.TourAirRealTimeDataDTO;
import com.weatherwhere.airservice.repository.airrealtime.RealTimeAirRepository;
import com.weatherwhere.airservice.service.GetTmXYAndStationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourRankAirDataServiceImpl implements TourRankAirDataService {
    private final GetTmXYAndStationService getTmXYAndStationService;
    private final RealTimeAirRepository realTimeAirRepository;

    private String getGrade(int Grade) {
        String grade ="";

        switch (Grade) {
            case 1 :
                grade = "좋음";
                break;
            case 2 :
                grade = "보통";
                break;
            case 3 :
                grade = "나쁨";
                break;
            case 4 :
                grade = "매우나쁨";
                break;
            default:
                break;
        }

        return grade;
    }

    // 위경도를 바탕으로 tour에 필요한 대기 실시간 DB 데이터 호출
    @Override
    @Transactional
    public ResultDTO<TourAirRealTimeDataDTO> getRealTimeAirDBData(Double x, Double y) {

        // 경도와 위도로 측정소명 조회
        String stationName = getTmXYAndStationService.getStationName(x, y);
        log.info("측정소 명 : {}", stationName);
        // 측정소명으로 데이터 조회
        RealTimeAirEntity realTimeAirEntity = realTimeAirRepository.findById(stationName).orElseThrow(() -> new NoSuchElementException());

        log.info("측정소명으로 조회한 데이터 : {}", realTimeAirEntity);

        // 등급 조회
        String pm10Grade = getGrade(realTimeAirEntity.getPm10Grade());
        String pm25Grade = getGrade(realTimeAirEntity.getPm25Grade());

        TourAirRealTimeDataDTO tourAirRealTimeDataDTO = TourAirRealTimeDataDTO.builder()
            .dataTime(realTimeAirEntity.getDataTime())
            .stationName(realTimeAirEntity.getStationName())
            .pm10Grade(pm10Grade)
            .pm10Value(realTimeAirEntity.getPm10Value())
            .pm25Value(realTimeAirEntity.getPm25Value())
            .build();
        log.info("tourAirRealTimeDataDTO : {}", tourAirRealTimeDataDTO);
        return ResultDTO.of(HttpStatus.OK.value(), "Tour - 실시간 대기정보를 조회하는데 성공하였습니다.", tourAirRealTimeDataDTO);
    }
}
