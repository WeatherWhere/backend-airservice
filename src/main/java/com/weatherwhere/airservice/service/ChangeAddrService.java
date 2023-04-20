package com.weatherwhere.airservice.service;

import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;

import java.util.List;

public interface ChangeAddrService {
    String changeAddr(String addr);
    ResultDTO<List<AirForecastDTO>> getFiveDaysData(String addr, String baseDate) throws Exception;
}
