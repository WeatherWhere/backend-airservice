package com.weatherwhere.airservice.realtimeair;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.service.airrealtime.ParseCSVService;
import com.weatherwhere.airservice.service.airrealtime.RealTimeAirService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.List;

@SpringBootTest
public class MakeEntityTest {
    @Autowired
    private RealTimeAirService realTimeAirService;
    @Autowired
    private ParseCSVService parseCSVService;

    @Test
    @DisplayName("makeEntityList 테스트")
    public void testmakeEntityList() throws ParseException, org.json.simple.parser.ParseException {
        long start = System.nanoTime();
        List<RealTimeAirEntity> realTimeAirEntityList = realTimeAirService.makeEntityList(parseCSVService.ParseCSV());
        long openApiEnd = System.nanoTime();
        System.out.println("OpenAPI 호출을 통해 Entity리스트를 만드는데 소요되는시간: " + (openApiEnd - start) + "ns");
        System.out.println(realTimeAirEntityList);
    }
}
