package com.weatherwhere.airservice.service;

import com.weatherwhere.airservice.dto.AddrDTO;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;
import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDTO;
import com.weatherwhere.airservice.service.airforecast.GetAirForecastDataService;
import com.weatherwhere.airservice.service.airrealtime.ParseCSVService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ChangeAddrServiceImpl implements ChangeAddrService{
    private final ParseCSVService parseCSVService;
    private final GetAirForecastDataService getAirForecastDataService;

    @Override
    @Cacheable("addrList")
    public String changeAddr(String addr) {
        List<AddrDTO> addrList = parseCSVService.addrParseCSV();
        String line = addr;
        String result = null;
        String[] data = line.split(" ");

        if (addr.contains("서울")) {
            result = data[0];;
        }else {
            String regex = ".{1}$";
            result = data[1].replaceAll(regex, "");
        }

        for (AddrDTO addrDTO : addrList) {
            if (result.equals(addrDTO.getCity())) {
               return addrDTO.getRegionName();
            }
        }
        return null;
    }

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
