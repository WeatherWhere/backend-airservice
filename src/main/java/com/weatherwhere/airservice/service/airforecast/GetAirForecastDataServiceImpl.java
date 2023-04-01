package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.domain.airforecast.AirForecastId;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDto;
import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDto;
import com.weatherwhere.airservice.dto.ResultDto;
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
    public ResultDto<Object> getSevenDaysDataOfLocation(SearchAirForecastDto searchAirForecastDto){
        AirForecastId airForecastId=new AirForecastId();
        airForecastId.setBaseDate(searchAirForecastDto.getBaseDate());
        airForecastId.setCity(searchAirForecastDto.getCity());


        List<AirForecastDto> sevenDaysData=new ArrayList<>();
        try{
            for(int i=0; i<7; i++){

                LocalDate searchDate=airForecastId.getBaseDate().plusDays(i);// 호출한 LocalDate 객체에 일(day)이 더해진 LocalDate 객체를 반환합니다.
                AirForecastId searchId=new AirForecastId(searchDate,airForecastId.getCity());

                // db에서 해당 날짜가 없을 때  예외처리!
                AirForecastEntity airForecastEntity=airForecastRepository.findByAirForecastId(searchId)
                    .orElseThrow(() -> new NoSuchElementException());
                sevenDaysData.add(entityToDto(airForecastEntity));
            }
            log.info("주간예보 개수: {}",sevenDaysData.size());
            log.info("7일의 주간예보: {}",sevenDaysData);
            return ResultDto.of(HttpStatus.OK.value(),"대기 주간예보 7일 데이터를 조회하는데 성공하였습니다.",sevenDaysData);
        }catch (NoSuchElementException e){
            // db에서 찾는 데이터 없을 경우
            e.getStackTrace();
            log.error("db에 7일의 주간예보 데이터 없음");
            return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "NoSuchElementException이 발생했습니다.", null);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 에러가 발생했습니다.", null);
        }
    }
}
