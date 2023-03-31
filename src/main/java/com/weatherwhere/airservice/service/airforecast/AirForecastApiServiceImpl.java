package com.weatherwhere.airservice.service.airforecast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.domain.airforecast.AirForecastId;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDto;
import com.weatherwhere.airservice.dto.response.ResultDto;
import com.weatherwhere.airservice.repository.airforecast.AirForecastRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AirForecastApiServiceImpl implements AirForecastApiService {

    private final AirForecastRepository airForecastRepository;

    // String-> LocalDate
    private LocalDate StringToLocalDate(String stringDate) throws java.text.ParseException {
        // DateTimeFormatter.ISO_DATE는 "yyyy-mm-dd"를 상수로 선언한 것
        LocalDate parseDate=LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
        return parseDate;
    }

    // 공공데이터 api url
    private String makeUrl(LocalDate date){
        String BASE_URL="https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustWeekFrcstDspth";
        String serviceKey="?ServiceKey="+System.getProperty("AIR_FORECAST_SERVICE_KEY_DE");
        String returnType="&returnType=json";
        String numOfRows="&numOfRows=100";
        String pageNo="pageNo=1";
        String searchDate="&searchDate="+date;

        String url= BASE_URL+serviceKey+returnType+numOfRows+pageNo+searchDate;
        return url;
    }

    // JSON 파싱해서 4일 정보 저장
    private HashMap<LocalDate,String> jsonParsing(String result) throws ParseException, java.text.ParseException {
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

        // 4일 날짜
        LocalDate firstDate=StringToLocalDate((String)item.get("frcstOneDt"));
        LocalDate secondDate=StringToLocalDate((String)item.get("frcstTwoDt"));
        LocalDate thirdDate=StringToLocalDate((String)item.get("frcstThreeDt"));
        LocalDate fourthDate=StringToLocalDate((String)item.get("frcstFourDt"));

        HashMap<LocalDate,String> forecastData=new HashMap<>(); // date와 정보
        forecastData.put(firstDate,firstData);
        forecastData.put(secondDate,secondData);
        forecastData.put(thirdDate,thirdData);
        forecastData.put(fourthDate,fourthData);

        return forecastData;
    }

    // db 저장
    private List<AirForecastDto> saveDb(List<AirForecastDto> dtoList){
        List<AirForecastDto> resultDtoList=new ArrayList<>();

        for (AirForecastDto dto : dtoList){
            AirForecastEntity airForecastEntity=toEntity(dto);
            airForecastRepository.save(airForecastEntity);
            resultDtoList.add(toDto(airForecastEntity));
        }
        log.info("saveDB: {}",resultDtoList);

        return resultDtoList;
    }


    // 대기 주간예보 api 데이터 받아오기 & db 저장
    @Override
    public ResultDto<Object> getApiData(LocalDate date){
        List<AirForecastDto> dtoList=new ArrayList<>();

        RestTemplate restTemplate= new RestTemplate();
        try{
            // RestTemplate으로 JSON data 받아오기
            String result=  restTemplate.getForObject(makeUrl(date),String.class);

            // Json 파싱해서 4일 data 저장
            HashMap<LocalDate,String> data=jsonParsing(result);

            // 4일 데이터 가공해서 db에 넣기
            for(LocalDate key: data.keySet()){ // key: 날짜, value: 가공 전 데이터
                List<AirForecastDto> dataList=dataToDto(data.get(key), key);
                dtoList.addAll(saveDb(dataList));
            }
            log.info("대기 주간예보 호출 데이터:{}",data);
            return ResultDto.of(HttpStatus.OK.value(),"대기 주간예보 데이터를 저장하는데 성공하였습니다.",dtoList);

        }catch (IndexOutOfBoundsException e) {
            // 공공데이터 api에 없는 정보 호출했을 경우
            e.printStackTrace();
            log.error("IndexOutOfBoundsException이 발생");
            return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "IndexOutOfBoundsException이 발생했습니다.", null);
        } catch (ParseException | java.text.ParseException e) {
            // json 데이터 파싱할 때 error
            e.printStackTrace();
            log.error("ParseException이 발생");
            return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ParseException이 발생했습니다.", null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("예기치 못한 에러가 발생");
            return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 에러가 발생했습니다.", null);
        }
    }

    // String 가공하여 Dto에 넣어주는 메서드
    private List<AirForecastDto> dataToDto(String data, LocalDate date){
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
            AirForecastId airForecastId=new AirForecastId();
            airForecastId.setCity(nowData[0].trim());
            airForecastId.setBaseDate(date);
            dto=AirForecastDto.builder()
                .baseDate(airForecastId.getBaseDate())
                .city(airForecastId.getCity())
                .forecast(nowData[1].trim())
                .reliability(reliability)
                .build();
            dtoList.add(dto);
        }
        return dtoList;
    }

}
