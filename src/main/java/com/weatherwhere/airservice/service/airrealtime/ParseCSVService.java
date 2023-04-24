package com.weatherwhere.airservice.service.airrealtime;


import com.weatherwhere.airservice.dto.airforecast.AddrDTO;

import com.weatherwhere.airservice.dto.airrealtime.StationNameDTO;

import java.util.List;

public interface ParseCSVService {
    List<StationNameDTO> ParseCSV();

    List<AddrDTO> addrParseCSV();

}
