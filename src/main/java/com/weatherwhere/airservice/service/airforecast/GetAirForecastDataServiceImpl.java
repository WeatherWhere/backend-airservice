package com.weatherwhere.airservice.service.airforecast;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.weatherwhere.airservice.domain.AirForecastEntity;
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
    public List<AirForecastDto> getSevenDaysDataOfLocation(SearchAirForecastDto searchAirForecastDto){
        LocalDate startDate=searchAirForecastDto.getBaseDate();
        String city= searchAirForecastDto.getCity();
        List<AirForecastDto> sevenDaysData=new ArrayList<>();
        for(int i=0; i<7; i++){
            LocalDate searchDate=startDate.plusDays(i);// 호출한 LocalDate 객체에 일(day)이 더해진 LocalDate 객체를 반환합니다.
            AirForecastEntity airForecastEntity=airForecastRepository.findByBaseDateAndCity(searchDate,city);
            sevenDaysData.add(entityToDto(airForecastEntity));
        }
        return sevenDaysData;
    }
}
