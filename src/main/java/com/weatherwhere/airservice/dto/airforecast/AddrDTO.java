package com.weatherwhere.airservice.dto.airforecast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * csv 파일로부터 지역 이름, 시군구 이름을 읽은 데이터를 DTO로 생성
 */
public class AddrDTO {
    String regionName;
    String city;
}
