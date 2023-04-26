package com.weatherwhere.airservice.service.airrealtime;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airrealtime.TourAirRealTimeDataDTO;
import com.weatherwhere.airservice.repository.airrealtime.RealTimeAirRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class TourRankAirDataServiceImpl implements TourRankAirDataService {
    private final GetTmXYAndStationService getTmXYAndStationService;
    private final RealTimeAirRepository realTimeAirRepository;

    /**
     * 미세먼지, 초미세먼지 등급의 값을 숫자에서 문자로 변환한 문자열을 리턴합니다.
     *
     * @param Grade 미세먼지 or 초미세먼지 등급
     * @return 숫자 등급에서 문자 등급으로 변환한 문자열을 리턴
     */
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

    /**
     * 경도 x, 위도 y에 해당하는 대기 실시간 데이터를 DB에서 조회한 값을 투어에서 사용할  ResultDTO<TourAirRealTimeDataDTO>로 리턴합니다.
     *
     * @param x 조회할 경도
     * @param y 조회할 위도
     * @return DB에서 해당 대기 실시간 데이터를 조회했다면 ResultDTO<TourAirRealTimeDataDTO> 리턴
     */
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
            .pm25Grade(pm25Grade)
            .build();
        log.info("tourAirRealTimeDataDTO : {}", tourAirRealTimeDataDTO);
        return ResultDTO.of(HttpStatus.OK.value(), "Tour - 실시간 대기정보를 조회하는데 성공하였습니다.", tourAirRealTimeDataDTO);
    }
}
