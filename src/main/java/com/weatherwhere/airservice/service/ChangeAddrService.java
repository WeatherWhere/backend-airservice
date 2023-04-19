package com.weatherwhere.airservice.service;

import java.util.List;

import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;

public interface ChangeAddrService {
    String changeAddr(String addr);
    ResultDTO<List<AirForecastDTO>> getTest(String addr, String baseDate) throws Exception;
}
