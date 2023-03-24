package com.weatherwhere.airservice.service.airforecast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weatherwhere.airservice.domain.AirForecastEntity;
import com.weatherwhere.airservice.dto.AirForecastDto;
import com.weatherwhere.airservice.repository.AirForecastRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AirForecastApiServiceImpl implements AirForecastApiService {

    private final AirForecastRepository airForecastRepository;

    // String-> LocalDate
    private LocalDate StringToLocalDate(String stringDate) throws java.text.ParseException {
        // DateTimeFormatter.ISO_DATE는 "yyyy-mm-dd"를 상수로 선언한 것
        LocalDate parseDate=LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
        System.out.println(parseDate);
        return parseDate;
    }
    // 대기 주간예보 api 데이터 받아오는 메서드
    @Override
    public List<AirForecastDto> getApiData(JSONObject date) throws
        ParseException, java.text.ParseException {
        String BASE_URL="https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustWeekFrcstDspth";
        String serviceKey="?ServiceKey="+System.getProperty("AIR_FORECAST_SERVICE_KEY_DE"); // 아직 환경변수 설정 전
        String returnType="&returnType=json";
        String numOfRows="&numOfRows=100";
        String pageNo="pageNo=1";
        String searchDate="&searchDate="+date.get("date");

        String url= BASE_URL+serviceKey+returnType+numOfRows+pageNo+searchDate; // 시간은 어떻게 해줄지 나중에!

        RestTemplate restTemplate= new RestTemplate();
        // RestTemplate으로 JSON data 받아오기
        String result=  restTemplate.getForObject(url,String.class);

        // JSON 파싱
        JSONParser jsonParser=new JSONParser();
        JSONObject jsonObject=(JSONObject)jsonParser.parse(result);
        JSONObject response=(JSONObject)jsonObject.get("response");
        JSONObject body=(JSONObject)response.get("body");
        JSONArray items=(JSONArray)body.get("items");

        JSONObject item=((JSONObject)items.get(0)); // items Array에는 하나만 들어있음

        // 4일의 정보
        String firstData=(String)item.get("frcstOneCn");
        String secondData=(String)item.get("frcstTwoCn");
        String thirdData=(String)item.get("frcstThreeCn");
        String fourthData=(String)item.get("frcstFourCn");

        List<AirForecastDto> dtoList=new ArrayList<>();
        // Json에서 String으로 받은 날짜-> LocalDate로 변환해서 넣음
        dtoList.addAll(dataToDto(firstData,StringToLocalDate((String)item.get("frcstOneDt"))));
        dtoList.addAll(dataToDto(secondData,StringToLocalDate((String)item.get("frcstTwoDt"))));
        dtoList.addAll(dataToDto(thirdData,StringToLocalDate((String)item.get("frcstThreeDt"))));
        dtoList.addAll(dataToDto(fourthData,StringToLocalDate((String)item.get("frcstFourDt"))));

        //dto리스트를 entity리스트로 변환하는 부분

        for (AirForecastDto dto : dtoList){
            // 엔티티에 해당 date에 값이 존재하는지 판별하기
            AirForecastEntity airForecastEntity=airForecastRepository.findByBaseDateAndCity(dto.getBaseDate(),dto.getCity());

            if(airForecastEntity != null){  // 해당 날짜가 존재할 경우 엔티티 업데이트
                airForecastEntity.update(dto);
                // DB 저장
                airForecastRepository.save(airForecastEntity);
            }else{// 해당 날짜 존재 안 할 경우 새로 생성
                AirForecastEntity entity=toEntity(dto);
                // DB 저장
                airForecastRepository.save(entity);
            }
        }
        // db에 저장
       // airForecastRepository.saveAll(entityList);
        return dtoList;
    }

    // String 가공하여 Dto에 넣어주는 메서드
    @Override
    public List<AirForecastDto> dataToDto(String data, LocalDate date){
        /*
         * "서울 : 높음, 인천 : 높음, 경기북부 : 높음, 경기남부 : 높음, 강원영서 : 높음, 강원영동 : 낮음
         * , 대전 : 낮음, 세종 : 낮음, 충남 : 높음, 충북 : 높음, 광주 : 낮음, 전북 : 낮음
         * , 전남 : 낮음, 부산 : 낮음, 대구 : 낮음, 울산 : 낮음, 경북 : 낮음, 경남 : 낮음, 제주 : 낮음, 신뢰도 : 보통",
         * 이런식으로 들어있는 데이터 가공
         * */
        String [] city=data.split(",");

        String reliability=city[city.length-1].split(" : ")[1].trim(); // 맨 마지막 신뢰도 저장!

        AirForecastDto dto=new AirForecastDto();
        List<AirForecastDto> dtoList=new ArrayList<>();
        // 해당 날짜에 cirt[i]: "서울 : 높음" 를 가공해서 dtoList에 넣는다.
        for(int i=0; i<city.length-2; i++){//cirt[i]: "서울 : 높음"
            String nowData[]=city[i].split(" : ");
            dto=AirForecastDto.builder()
                .baseDate(date)
                .city(nowData[0].trim())
                .forecast(nowData[1].trim())
                .reliability(reliability)
                .build();

            dtoList.add(dto);
        }
        return dtoList;
    }

}
