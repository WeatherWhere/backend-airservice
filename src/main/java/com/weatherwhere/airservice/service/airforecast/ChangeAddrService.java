package com.weatherwhere.airservice.service.airforecast;

import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;

import java.util.List;

public interface ChangeAddrService {
    /**
     * 문자열의 행정동 주소를 주간예보에서 검색이 가능한 도(시) 주소로 변환한 문자열을 리턴합니다.
     *
     * @param addr 행정동까지의 주소
     * @return 행정동 주소를 도(시)주소로 변환한 주소를 리턴
     */
    String changeAddr(String addr);


    /**
     * 변환한 주소와 검색할 날짜부터 DB에서 5일의 주간 예보 데이터를 조회하여 ResultDTO<List<AirForecastDTO>>를 리턴합니다.
     *
     * @param addr 도(시) 주소
     * @param baseDate 오늘 날짜
     * @return 5일의 주간 예보 데이터를 db에서 조회하는데 성공했다면 ResultDTO<List<AirForecastDTO>> 리턴, 그렇지 않다면 예외처리
     * @throws Exception
     */
    ResultDTO<List<AirForecastDTO>> getFiveDaysData(String addr, String baseDate) throws Exception;

}
