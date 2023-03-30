package com.weatherwhere.airservice.airforecast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDto;
import com.weatherwhere.airservice.service.airforecast.AirForecastApiService;
import com.weatherwhere.airservice.service.airforecast.AirForecastApiServiceImpl;
import com.weatherwhere.airservice.service.airforecast.GetAirForecastDataServiceImpl;

@SpringBootTest
public class AitForecastTests {
    @Autowired
    private AirForecastApiService airForecastApiService;

    @Autowired
    private GetAirForecastDataServiceImpl getAirForecastDataService;

    static LocalDate date;
    @BeforeEach
    void beforeSetUp(){
        // 오늘 날짜 전날
        date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        String formatted = date.format(formatter);
        date=date.minusDays(1);
    }



    @Test
    @DisplayName("대기 주간예보 api를 호출하고 db에 저장하는 테스트")
    public void testGetApi() throws ParseException, java.text.ParseException {
        /*
        // 오늘 날짜 전날
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        String formatted = date.format(formatter);
        date=date.minusDays(1);
        System.out.println("date:     "+date);*/

        // 아직 임시로 print하고 나중에 response에 성공여부 출력해보기
        System.out.println(airForecastApiService.getApiData(date));
       // Assertions.assertEquals();
    }

    @Test
    @DisplayName("7일의 주간예보 DB에서 조회하는 테스트")
    public void testGetSevenDaysData(){
        SearchAirForecastDto searchAirForecastDto=new SearchAirForecastDto(date,"서울");

        // 아직 임시로 print하고 나중에 response에 성공여부 출력해보기
        System.out.println(getAirForecastDataService.getSevenDaysDataOfLocation(searchAirForecastDto));
        // Assertions.assertEquals();
    }
}
