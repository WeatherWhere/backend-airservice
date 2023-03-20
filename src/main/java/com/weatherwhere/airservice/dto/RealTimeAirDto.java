package com.weatherwhere.airservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class RealTimeAirDto {
    private Response response;
    private String stationName;

    @Data
    public static class Response {
        private Body body;

        @Data
        public static class Body {
            private List<Item> items;

            @Data
            public static class Item {
                private int so2Grade;
                private int khaiValue;
                private float so2Value;
                private float coValue;
                private int pm10Value;
                private int o3Grade;
                private int khaiGrade;
                private int pm25Value;
                private int no2Grade;
                private int pm25Grade;
                private String dataTime;
                private int coGrade;
                private float no2Value;
                private int pm10Grade;
                private float o3Value;

                //String 타입으로 받아온 dataTime을 LocalDateTime 타입으로 변환
                public LocalDateTime getDateTimeParsed() {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime dateTime = LocalDateTime.parse(dataTime, formatter);
                    return dateTime;
                }
            }
        }
    }
}
