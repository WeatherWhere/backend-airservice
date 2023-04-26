package com.weatherwhere.airservice.service.airrealtime;


import com.weatherwhere.airservice.dto.airforecast.AddrDTO;

import com.weatherwhere.airservice.dto.airrealtime.StationNameDTO;

import java.util.List;

public interface ParseCSVService {
    /**
     * CSV 파일을 읽어 StationNameDTO 객체를 생성하고, 이를 담고 있는 List(List<StationNameDTO>)를 리턴합니다.
     *
     * @return 측정소 명 List(List<StationNameDTO>)를 리턴
     */
    List<StationNameDTO> ParseCSV();

    /**
     * CSV 파일을 읽어 AddrDTO 객체를 생성하고, 이를 담고 있는 List(List<AddrDTO>)를 리턴합니다.
     *
     * @return 주소 List(List<AddrDTO>)를 리턴
     */
    List<AddrDTO> addrParseCSV();

}
