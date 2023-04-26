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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.weatherwhere.airservice.domain.airforecast.AirForecastEntity;
import com.weatherwhere.airservice.domain.airforecast.AirForecastId;
import com.weatherwhere.airservice.dto.airforecast.AirForecastDTO;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.repository.airforecast.AirForecastRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class AirForecastApiServiceImpl implements AirForecastApiService {

    private final AirForecastRepository airForecastRepository;

    /**
     * String으로 받은 날짜를 LocalDate로 타입을 변환한 LocalDate를 리턴합니다.
     *
     * @param stringDate String 타입의 날짜
     * @return
     * @throws java.text.ParseException
     */
    private LocalDate stringToLocalDate(String stringDate) throws java.text.ParseException {
        // DateTimeFormatter.ISO_DATE는 "yyyy-mm-dd"를 상수로 선언한 것
        LocalDate parseDate = LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
        return parseDate;
    }

    /**
     * 주어진 날짜를 가지고 대기 주간 예보 공공데이터 api url를 리턴합니다.
     *
     * @param date url에 필요한 호출할 찾을 날짜
     * @return 날짜와 함께 url를 만들었다면 String 타입의 url를 리턴
     */
    private String makeUrl(LocalDate date){
        String BASE_URL = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustWeekFrcstDspth";
        String serviceKey = "?ServiceKey="+System.getProperty("AIR_FORECAST_SERVICE_KEY_DE");
        String returnType = "&returnType=json";
        String numOfRows = "&numOfRows=100";
        String pageNo = "pageNo=1";
        String searchDate = "&searchDate="+date;

        String url = BASE_URL + serviceKey + returnType + numOfRows + pageNo + searchDate;
        return url;
    }

    /**
     * 호출받은 문자열을 파싱해서 4일의 주간예보 데이터로 HashMap<LocalDate, String>을 리턴합니다.
     *
     * @param result api를 호출하여 받은 json 값
     * @return 문자열을 4일의 주간예보 데이터로 변환하였다면 HashMap<LocalDate, String>를 리턴, 그렇지 않다면 예외를 throw
     * @throws ParseException
     * @throws java.text.ParseException
     */
    private HashMap<LocalDate,String> jsonParsing(String result) throws ParseException, java.text.ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONArray items = (JSONArray)body.get("items");

        JSONObject item = ((JSONObject)items.get(0)); // items Array에는 하나만 들어있음

        // 4일의 정보
        String firstData = (String)item.get("frcstOneCn");
        String secondData = (String)item.get("frcstTwoCn");
        String thirdData = (String)item.get("frcstThreeCn");
        String fourthData = (String)item.get("frcstFourCn");

        // 4일 날짜
        LocalDate firstDate = stringToLocalDate((String)item.get("frcstOneDt"));
        LocalDate secondDate = stringToLocalDate((String)item.get("frcstTwoDt"));
        LocalDate thirdDate = stringToLocalDate((String)item.get("frcstThreeDt"));
        LocalDate fourthDate = stringToLocalDate((String)item.get("frcstFourDt"));

        HashMap<LocalDate,String> forecastData=new HashMap<>(); // date와 정보
        forecastData.put(firstDate,firstData);
        forecastData.put(secondDate,secondData);
        forecastData.put(thirdDate,thirdData);
        forecastData.put(fourthDate,fourthData);

        return forecastData;
    }

    /**
     * List<AirForcastDTO>를 db에 업데이트한 값을 List<AirForecastDTO>로 리턴합니다.
     *
     * @param dtoList db에 업데이트할 대기 주간 예보 데이터 리스트
     * @return 대기 주간 예보 데이터를 db에 업데이트 성공했다면 List<AirForecastDTO> 리턴
     */
    private List<AirForecastDTO> saveDb(List<AirForecastDTO> dtoList) {
        List<AirForecastDTO> resultDtoList = new ArrayList<>();

        for (AirForecastDTO dto : dtoList) {
            AirForecastEntity airForecastEntity = toEntity(dto);
            airForecastRepository.save(airForecastEntity);
            resultDtoList.add(toDto(airForecastEntity));
        }
        log.info("saveDB : {}",resultDtoList);
        return resultDtoList;
    }

    /**
     * 검색할 날짜 LocalDate와 함께 공공데이터 api를 호출하여 받은 데이터를 파싱하여 db에 업데이트한 대기 주간 예보 데이터, ResultDTO<List<AirForecastDTO>>를 리턴합니다.
     *
     * @param date 검색할 날짜
     * @return api를 호출하고 데이터를 파싱하여 db에 업데이트를 성공했다면 ResultDTO<List<AirForecastDTO>> 리턴, 그렇지 않다면 예외처리
     */
    @Override
    @Transactional
    public ResultDTO<List<AirForecastDTO>> getApiData(LocalDate date) {
        List<AirForecastDTO> dtoList = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        try {
            // RestTemplate으로 JSON data 받아오기
            String result = restTemplate.getForObject(makeUrl(date),String.class);

            // Json 파싱해서 4일 data 저장
            HashMap<LocalDate,String> data = jsonParsing(result);

            // 4일 데이터 가공해서 db에 넣기
            for(LocalDate key: data.keySet()){ // key: 날짜, value: 가공 전 데이터
                List<AirForecastDTO> dataList = dataToDto(data.get(key), key);
                dtoList.addAll(saveDb(dataList));
            }
            log.info("대기 주간예보 호출 데이터 : {}",data);
        } catch (IndexOutOfBoundsException e) {
            // 공공데이터 api에 없는 정보 호출했을 경우
            e.printStackTrace();
            log.warn("IndexOutOfBoundsException이 발생");
        } catch (ParseException | java.text.ParseException e) {
            // json 데이터 파싱할 때 error
            e.printStackTrace();
            log.warn("ParseException이 발생");
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("예기치 못한 에러가 발생");
        }
        return ResultDTO.of(HttpStatus.OK.value(),"대기 주간예보 데이터를 저장하는데 성공하였습니다.",dtoList);
    }


    /**
     * 해당 날짜 LocalDate에 해당하는 String 데이터를 가공하여 List<AirForecastDTO>로 리턴합니다.
     *
     * @param data 해당 날짜에 해당하는 json을 파싱하여 얻은 가공이 필요한 주간 예보 데이터
     * @param date 예보 날짜
     * @return data를 주간 예보 데이터로 가공을 하였다면 List<AirForecastDTO> 리턴
     */
    private List<AirForecastDTO> dataToDto(String data, LocalDate date) {
        /*
         * "서울 : 높음, 인천 : 높음, 경기북부 : 높음, 경기남부 : 높음, 강원영서 : 높음, 강원영동 : 낮음
         * , 대전 : 낮음, 세종 : 낮음, 충남 : 높음, 충북 : 높음, 광주 : 낮음, 전북 : 낮음
         * , 전남 : 낮음, 부산 : 낮음, 대구 : 낮음, 울산 : 낮음, 경북 : 낮음, 경남 : 낮음, 제주 : 낮음, 신뢰도 : 보통",
         * 이런식으로 들어있는 데이터 가공
         * */
        String [] city = data.split(",");

        String reliability = city[city.length-1].split(" : ")[1].trim(); // 맨 마지막 신뢰도 저장!

        AirForecastDTO dto = new AirForecastDTO();
        List<AirForecastDTO> dtoList = new ArrayList<>();
        // 해당 날짜에 cirt[i]: "서울 : 높음" 를 가공해서 dtoList에 넣는다.
        for (int i = 0; i<city.length-2; i++) {//cirt[i]: "서울 : 높음"
            String nowData[] = city[i].split(" : ");
            AirForecastId airForecastId = new AirForecastId();
            airForecastId.setCity(nowData[0].trim());
            airForecastId.setBaseDate(date);
            dto = AirForecastDTO.builder()
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
