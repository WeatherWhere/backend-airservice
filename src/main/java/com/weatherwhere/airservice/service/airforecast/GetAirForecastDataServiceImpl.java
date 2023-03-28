package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weatherwhere.airservice.domain.AirForecastEntity;
import com.weatherwhere.airservice.domain.AirForecastId;
import com.weatherwhere.airservice.dto.AirForecastDto;
import com.weatherwhere.airservice.dto.SearchAirForecastDto;
import com.weatherwhere.airservice.repository.AirForecastRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAirForecastDataServiceImpl implements GetAirForecastDataService{
    private final AirForecastRepository airForecastRepository;

    // 해당 위치 7일 대기오염 주간예보 DB 가져오기
    @Override
    @Transactional
    public List<AirForecastDto> getSevenDaysDataOfLocation(SearchAirForecastDto searchAirForecastDto) throws Exception {
        AirForecastId airForecastId=new AirForecastId();
        airForecastId.setBaseDate(searchAirForecastDto.getBaseDate());
        airForecastId.setCity(searchAirForecastDto.getCity());


        List<AirForecastDto> sevenDaysData=new ArrayList<>();
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
        return sevenDaysData;
    }
}
