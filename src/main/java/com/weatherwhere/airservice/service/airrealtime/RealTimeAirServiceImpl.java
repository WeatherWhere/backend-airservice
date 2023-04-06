package com.weatherwhere.airservice.service.airrealtime;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.ResultDto;
import com.weatherwhere.airservice.dto.airrealtime.RealTimeAirDto;
import com.weatherwhere.airservice.repository.airrealtime.RealTimeAirRepository;
import com.weatherwhere.airservice.repository.airrealtime.StationNameRepository;
import com.weatherwhere.airservice.service.GetTmXYAndStationServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Log4j2
@RequiredArgsConstructor
public class RealTimeAirServiceImpl implements RealTimeAirService {

    private final RealTimeAirRepository realTimeAirRepository;
    private final GetTmXYAndStationServiceImpl getTmXYAndStationService;

    //JSON 파싱
    public Object JsonParser(String jsonString) throws org.json.simple.parser.ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONArray items = (JSONArray) body.get("items");

        JSONObject item = ((JSONObject) items.get(0));

        // item의 요소들의 값이 "-" 이나 null일때 "0"을 넣어줌
        // 다른 값을 넣을게 있다면 그걸 넣겠음
        for (Object key : item.keySet()) {
            Object value = item.get(key);

            if (value == null || value.toString().equals("-")) {
                item.put(key.toString(), "0");
            }
        }
        return item;
    }

    @Override
    //Open API에서 데이터 받아오고 파싱
    public Object getRealTimeAirData(String stationName) throws ParseException, org.json.simple.parser.ParseException {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
                "?stationName=" + stationName +
                "&dataTerm=daily" +
                "&pageNo=1" +
                "&numOfRows=1" +
                "&returnType=json" +
                "&serviceKey=" + System.getProperty("AIR_FORECAST_SERVICE_KEY_DE") +
                "&ver=1.0";
        String jsonString = restTemplate.getForObject(apiUrl, String.class);

        Object result = JsonParser(jsonString);
        return result;
    }

    //받아온 데이터를 DB에 저장
    @Override
    @Transactional
    public RealTimeAirDto saveRealTimeAirData(String stationName) {
        RealTimeAirDto realTimeAirDto = new RealTimeAirDto();
        try {
            Object realTimeAirData = getRealTimeAirData(stationName);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            realTimeAirDto = RealTimeAirDto.builder()
                    .stationName(stationName)
                    .dataTime(LocalDateTime.parse((String) ((JSONObject) realTimeAirData).get("dataTime"), dateFormatter))
                    .so2Value(Double.parseDouble((String) ((JSONObject) realTimeAirData).get("so2Value")))
                    .coValue(Double.parseDouble((String) ((JSONObject) realTimeAirData).get("coValue")))
                    .o3Value(Double.parseDouble((String) ((JSONObject) realTimeAirData).get("o3Value")))
                    .no2Value(Double.parseDouble((String) ((JSONObject) realTimeAirData).get("no2Value")))
                    .pm10Value(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("pm10Value")))
                    .pm25Value(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("pm25Value")))
                    .khaiValue(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("khaiValue")))
                    .so2Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("so2Grade")))
                    .coGrade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("coGrade")))
                    .o3Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("o3Grade")))
                    .no2Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("no2Grade")))
                    .pm10Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("pm10Grade")))
                    .pm25Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("pm25Grade")))
                    .khaiGrade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("khaiGrade")))
                    .build();

            //RealTimeAirEntity 객체 생성
            RealTimeAirEntity realTimeAirEntity = ToEntity(realTimeAirDto);
            realTimeAirRepository.save(realTimeAirEntity);

            log.info("실시간 대기정보 호출 데이터:{}", realTimeAirDto);
        } catch (IndexOutOfBoundsException e) {
            // 공공데이터 api에 없는 정보 호출했을 경우
            e.printStackTrace();
            log.error("IndexOutOfBoundsException이 발생", stationName);
        } catch (Exception e) {
            if (e instanceof org.json.simple.parser.ParseException) {
                // json 데이터 파싱할 때 error
                e.printStackTrace();
                log.error("ParseException이 발생", stationName);
            } else {
                e.printStackTrace();
                log.error("예기치 못한 에러가 발생", stationName);
            }
        }
        return realTimeAirDto;
    }



    //DB에서 측정소 명을 가져와서 변수로 사용해 데이터를 갱신
    @Override
    @Transactional
    public Object updateRealtimeAirDate() throws ParseException, org.json.simple.parser.ParseException {
        // 갱신된 데이터 저장할 리스트
        List<Object> updatedDataList = new ArrayList<>();

        // 저장된 측정소 이름 가져오기
        List<String> stationNames = realTimeAirRepository.getStationNames();

        // 측정소 이름별로 데이터 저장하기
        for (String stationName : stationNames) {
            Object updateData = saveRealTimeAirData(stationName);
            updatedDataList.add(updateData);
        }
        return updatedDataList;
    }

    //DB에서 데이터 가져오기
    @Override
    @Transactional
    public ResultDto<List<RealTimeAirEntity>> getRealTimeDBData(Double x, Double y) throws org.json.simple.parser.ParseException {
        List<RealTimeAirEntity> List = new ArrayList<>();
        String stationName = getTmXYAndStationService.getStationName(x, y);
        RealTimeAirEntity result = realTimeAirRepository.findById(stationName).orElseThrow(() -> new NoSuchElementException());
        List.add(result);
        return ResultDto.of(HttpStatus.OK.value(), "실시간 대기정보를 조회하는데 성공하였습니다.", List);
    }

    //DB에 stationName을 csv에서 읽어와 저장하는 메서드
    private final StationNameRepository stationNameRepository;

    public String readStationName() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("station_list.csv"));
        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            RealTimeAirEntity stationName = new RealTimeAirEntity();
            stationName.setStationName(data[1]);
            stationNameRepository.save(stationName);
        }
        reader.close();

        return "성공";
    }
}
