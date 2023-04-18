package com.weatherwhere.airservice.service.airrealtime;

import com.weatherwhere.airservice.dto.AddrDto;
import com.weatherwhere.airservice.dto.airrealtime.StationNameDto;

import java.util.List;

public interface ParseCSVService {
    List<StationNameDto> ParseCSV();

    List<AddrDto> addrParseCSV();
}
