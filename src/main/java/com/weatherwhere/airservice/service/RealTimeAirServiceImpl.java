package com.weatherwhere.airservice.service;

import com.weatherwhere.airservice.domain.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.RealTimeAirDto;
import com.weatherwhere.airservice.repository.RealTimeAirRepository;
import com.weatherwhere.airservice.repository.StationNameRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class RealTimeAirServiceImpl implements RealTimeService {

    private final RealTimeAirRepository realTimeAirRepository;

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
                "&serviceKey=" + "LKiVUvMq5P2zZ88FZoOq/h0k9y98k2pEdRcJSheoYPwZxYlcaGkQugApuMndBS0dqRg1QeziMPwW9rbVvRIcRA==" +
                //System.getProperty("AIR_FORECAST_SERVICE_KEY_DE") +
                "&ver=1.0";
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        try {
            Object result = JsonParser(jsonString);
            return result;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexE:" + stationName);
        }
        return "성공";
    }

    //받아온 데이터를 DB에 저장
    @Override
    @Transactional
    public Object saveRealTimeAirData(String stationName) throws ParseException, org.json.simple.parser.ParseException {
        Object realTimeAirData = getRealTimeAirData(stationName);
        try {
            RealTimeAirDto realTimeAirDto = RealTimeAirDto.builder()
                    .stationName(stationName)
                    .dataTime((String) ((JSONObject) realTimeAirData).get("dataTime"))
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

            return ToDto(realTimeAirEntity);
        } catch (ClassCastException e) {
            System.out.println("ClassE:" + stationName);
        }

        return "성공";
    }

    //DB에서 측정소 명을 가져와서 변수로 사용해 데이터를 갱신
    @Override
    @Transactional
    public Object updateRealtimeAirDate() throws ParseException, org.json.simple.parser.ParseException {
        // 저장된 측정소 이름 가져오기
        List<String> stationNames = realTimeAirRepository.getStationNames();

        // 측정소 이름별로 데이터 저장하기
        //중동(유해+중금속)은 데이터가 안 받아와져 그 다음으로 가까운 다른 곳의 데이터를 넣어줌
        for (String stationName : stationNames) {
            if (stationName.equals("중동(유해+중금속)")) {
                saveRealTimeAirData("고산리");
            } else {
                saveRealTimeAirData(stationName);
            }
        }

        return "성공";
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
