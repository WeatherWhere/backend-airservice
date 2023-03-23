package com.weatherwhere.airservice.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weatherwhere.airservice.domain.AirForecastEntity;
import com.weatherwhere.airservice.dto.AirForecastDto;
import com.weatherwhere.airservice.repository.AirForecastRepository;

@Service
public class AirForecastServiceImpl extends AirForecastService{
    @Autowired
    private AirForecastRepository airForecastRepository;

    // 대기 주간예보 api 데이터 받아오는 메서드
    @Override
    public List<AirForecastDto> getApiData(JSONObject date) throws
        ParseException,
        URISyntaxException,
        UnsupportedEncodingException {
        String BASE_URL="https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustWeekFrcstDspth";
        String serviceKey="?ServiceKey="+System.getProperty("AIR_FORECAST_SERVICE_KEY_DE"); // 아직 환경변수 설정 전
        String returnType="&returnType=json";
        String numOfRows="&numOfRows=100";
        String pageNo="pageNo=1";
        String searchDate="&searchDate="+date.get("date");

        String url= BASE_URL+serviceKey+returnType+numOfRows+pageNo+searchDate; // 시간은 어떻게 해줄지 나중에!

        RestTemplate restTemplate= new RestTemplate();
        URI endUrl=new URI(url);
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
        String one=(String)item.get("frcstOneCn");
        String two=(String)item.get("frcstTwoCn");
        String three=(String)item.get("frcstThreeCn");
        String four=(String)item.get("frcstFourCn");

        List<AirForecastDto> dtoList=new ArrayList<>();
        dtoList.addAll(dataToDto(one,(String)item.get("frcstOneDt")));
        dtoList.addAll(dataToDto(two,(String)item.get("frcstTwoDt")));
        dtoList.addAll(dataToDto(three,(String)item.get("frcstThreeDt")));
        dtoList.addAll(dataToDto(four,(String)item.get("frcstFourDt")));

        //dto리스트를 entity리스트로 변환하는 부분
        List<AirForecastEntity> entityList = new ArrayList<>();
        for (AirForecastDto dto : dtoList){
            // 엔티티에 해당 date에 값이 존재하는지 판별하기
            AirForecastEntity airForecastEntity=airForecastRepository.findByBaseDateAndCity(dto.getBaseDate(),dto.getCity());

            if(airForecastEntity!=null){  // 해당 날짜가 존재할 경우 엔티티 업데이트
                airForecastEntity.update(dto);
                entityList.add(airForecastEntity);
            }else{// 해당 날짜 존재 안 할 경우 새로 생성
                AirForecastEntity entity=toEntity(dto);
                entityList.add(entity);
            }
        }

        // db에 저장
        airForecastRepository.saveAll(entityList);

        return dtoList;
    }

    // String 가공하여 Dto에 넣어주는 메서드
    @Override
    public List<AirForecastDto> dataToDto(String data, String date){
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

    // DTO -> Entity
    @Override
    public AirForecastEntity toEntity(AirForecastDto dto){
        AirForecastEntity airForecastEntity = AirForecastEntity.builder()
            .baseDate(dto.getBaseDate())
            .city(dto.getCity())
            .forecast(dto.getForecast())
            .reliability(dto.getReliability())
            .build();
        return airForecastEntity;
    }
}
