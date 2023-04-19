package com.weatherwhere.airservice.service;

import com.weatherwhere.airservice.dto.ResultDto;

public interface ChangeAddrService {
    String changeAddr(String addr);
    ResultDto<Object> getTest(String addr, String baseDate) throws Exception;
}
