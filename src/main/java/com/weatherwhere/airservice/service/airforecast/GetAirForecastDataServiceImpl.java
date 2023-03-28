package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    public List<AirForecastDto> getSevenDaysDataOfLocation(SearchAirForecastDto searchAirForecastDto){
        AirForecastId airForecastId=new AirForecastId();
        airForecastId.setBaseDate(searchAirForecastDto.getAirForecastId().getBaseDate());
        airForecastId.setCity(searchAirForecastDto.getAirForecastId().getCity());

        List<AirForecastDto> sevenDaysData=new ArrayList<>();
        for(int i=0; i<7; i++){
            AirForecastId searchId=new AirForecastId();
            LocalDate searchDate=airForecastId.getBaseDate().plusDays(i);// 호출한 LocalDate 객체에 일(day)이 더해진 LocalDate 객체를 반환합니다.
            searchId.setBaseDate(searchDate);
            searchId.setCity(airForecastId.getCity());
            AirForecastEntity airForecastEntity=airForecastRepository.findByAirForecastId(searchId);
            sevenDaysData.add(entityToDto(airForecastEntity));
        }
        return sevenDaysData;
    }
}
