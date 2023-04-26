package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.weatherwhere.airservice.dto.airforecast.AddrDTO;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;
import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDTO;
import com.weatherwhere.airservice.service.airrealtime.ParseCSVService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChangeAddrServiceImpl implements ChangeAddrService {
    private final ParseCSVService parseCSVService;
    private final GetAirForecastDataService getAirForecastDataService;


    /**
     * 문자열의 행정동 주소를 주간예보에서 검색이 가능한 도(시) 주소로 변환한 문자열을 리턴합니다.
     *
     * @param addr 행정동까지의 주소
     * @return 행정동 주소를 도(시)주소로 변환한 주소를 리턴
     */
    @Override
    @Cacheable("addrList")
    public String changeAddr(String addr) {
        List<AddrDTO> addrList = parseCSVService.addrParseCSV();
        String line = addr;
        String result = null;
        String[] data = line.split(" ");
        String changeAddress = "";
        if (addr.contains("서울")) {
            result = data[0];;
        } else {
            String regex = ".{1}$";
            result = data[1].replaceAll(regex, "");
        }

        for (AddrDTO addrDTO : addrList) {
            if (result.equals(addrDTO.getCity())) {
               changeAddress = addrDTO.getRegionName();
            }
        }
        return changeAddress;
    }


    /**
     * 변환한 주소와 검색할 날짜부터 DB에서 5일의 주간 예보 데이터를 조회하여 ResultDTO<List<AirForecastDTO>>를 리턴합니다.
     *
     * @param addr 도(시) 주소
     * @param baseDate 오늘 날짜
     * @return 5일의 주간 예보 데이터를 db에서 조회하는데 성공했다면 ResultDTO<List<AirForecastDTO>> 리턴, 그렇지 않다면 예외처리
     * @throws Exception
     */
    @Override
    public ResultDTO<List<AirForecastDTO>> getFiveDaysData(String addr, String baseDate) throws Exception {
        String city = changeAddr(addr);
        if(city == null) {
            log.error("Could not find city for address: {}", addr);
            return ResultDTO.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not find city for address.", null);
        }

        SearchAirForecastDTO searchAirForecastDTO = new SearchAirForecastDTO();
        searchAirForecastDTO.setBaseDate(LocalDate.parse(baseDate));
        searchAirForecastDTO.setCity(city);
        return getAirForecastDataService.getFiveDaysDataOfLocation(searchAirForecastDTO);
    }
}
