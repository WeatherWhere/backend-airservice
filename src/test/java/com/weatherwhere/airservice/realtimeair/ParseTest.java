package com.weatherwhere.airservice.realtimeair;

import com.weatherwhere.airservice.service.airrealtime.ParseCSVService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ParseTest {
    @Autowired
    private ParseCSVService parseCSVService;

    @Test
    public void test() {
        /*
        List<StationNameDto> stationNameDtoList = parseCSVService.ParseCSV();
        for (StationNameDto stationNameDto: stationNameDtoList) {
            System.out.println(stationNameDto);
        }
         */

        System.out.println(parseCSVService.ParseCSV());
    }
}
