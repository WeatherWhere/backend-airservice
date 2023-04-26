package com.weatherwhere.airservice.dto.airrealtime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * CSV 파일로 부터 지역이름을 파싱한 데이터를 DTO로 생성
 */
public class StationNameDTO {
    private String stationName;
}
