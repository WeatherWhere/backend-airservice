package com.weatherwhere.airservice;

import com.weatherwhere.airservice.service.airrealtime.RealTimeAirServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AirserviceApplicationTests {
	@Autowired
	private RealTimeAirServiceImpl realTimeAirService;

	@Test
	void testStationNameRead() throws IOException {
        realTimeAirService.readStationName();
        System.out.println(realTimeAirService.readStationName());
    }
}
