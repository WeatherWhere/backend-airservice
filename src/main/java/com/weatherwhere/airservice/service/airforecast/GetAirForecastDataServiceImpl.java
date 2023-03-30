package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.domain.airforecast.AirForecastId;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDto;
import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDto;
import com.weatherwhere.airservice.repository.airforecast.AirForecastRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class GetAirForecastDataServiceImpl implements GetAirForecastDataService{
    private final AirForecastRepository airForecastRepository;

    // 해당 위치 7일 대기오염 주간예보 DB 가져오기
    @Override
    @Transactional
    public List<AirForecastDto> getSevenDaysDataOfLocation(SearchAirForecastDto searchAirForecastDto){
        AirForecastId airForecastId=new AirForecastId();
        airForecastId.setBaseDate(searchAirForecastDto.getBaseDate());
        airForecastId.setCity(searchAirForecastDto.getCity());


        List<AirForecastDto> sevenDaysData=new ArrayList<>();
        try{
            for(int i=0; i<7; i++){
                AirForecastId searchId=new AirForecastId();
                LocalDate searchDate=airForecastId.getBaseDate().plusDays(i);// 호출한 LocalDate 객체에 일(day)이 더해진 LocalDate 객체를 반환합니다.
                searchId.setBaseDate(searchDate);
                searchId.setCity(airForecastId.getCity());

                // db에서 해당 날짜가 없을 때  예외처리!
                AirForecastEntity airForecastEntity=airForecastRepository.findByAirForecastId(searchId)
                    .orElseThrow(() -> new NoSuchElementException());
                sevenDaysData.add(entityToDto(airForecastEntity));
            }
        }catch (NoSuchElementException e){
            // db에서 찾는 데이터 없을 경우
            e.getStackTrace();
            log.error("db에 7일의 주간예보 데이터 없음");
        }catch (Exception e){
            log.error(e.getMessage());
        }
        // 데이터가 DB에 없더라면 7개 안채워진채로 나갈 수 있음!
        log.info("주간예보 개수: {}",sevenDaysData.size());
        log.info("7일의 주간예보: {}",sevenDaysData);
        return sevenDaysData;
    }
}
