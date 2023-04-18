package com.weatherwhere.airservice.service;

import com.weatherwhere.airservice.dto.AddrDto;
import com.weatherwhere.airservice.dto.ResultDto;
import com.weatherwhere.airservice.dto.airforecast.SearchAirForecastDto;
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
        List<AddrDto> addrList = parseCSVService.addrParseCSV();
        System.out.println(addrList);
        String line = addr;
        String[] data = line.split(" ");
        String regex = ".{1}$";
        String result = data[1].replaceAll(regex, "");

        for (AddrDto addrDto : addrList) {
            if (result.equals(addrDto.getCity())) {
               return addrDto.getRegionName();
            }
        }
        return null;
    }

    @Override
    public ResultDto<Object> getTest(String addr, String baseDate) throws Exception {
        String city = changeAddr(addr);
        if(city == null) {
            log.error("Could not find city for address: {}", addr);
            return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not find city for address.", null);
        }
        SearchAirForecastDto searchAirForecastDto = new SearchAirForecastDto();
        searchAirForecastDto.setBaseDate(LocalDate.parse(baseDate));
        searchAirForecastDto.setCity(city);
        return getAirForecastDataService.getSevenDaysDataOfLocation(searchAirForecastDto);
    }
}
