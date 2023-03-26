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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                "&serviceKey=" + System.getProperty("AIR_FORECAST_SERVICE_KEY_DE") +
                "&ver=1.0";
        String jsonString = restTemplate.getForObject(apiUrl, String.class);
        try {
            Object result = JsonParser(jsonString);
            return result;
        } catch (IndexOutOfBoundsException e) {
            //IndexOutOfBoundsException 이 뜨는 이유는 측정소에서의 실시간 미세먼지 정보가 없음
            System.out.println("IndexE:" + stationName);
        }
        return "성공";
    }

    //받아온 데이터를 DB에 저장
    @Override
    @Transactional
    public Object saveRealTimeAirData(String stationName) throws ParseException, org.json.simple.parser.ParseException {
        Object realTimeAirData = getRealTimeAirData(stationName);
        RealTimeAirDto realTimeAirDto = null;
        try {
            //RealTimeAirDto 객체 생성
            realTimeAirDto = new RealTimeAirDto();
            realTimeAirDto.setStationName(stationName);
            realTimeAirDto.setDataTime((String) ((JSONObject) realTimeAirData).get("dataTime"));
            realTimeAirDto.setSo2Value(Double.parseDouble((String) ((JSONObject) realTimeAirData).get("so2Value")));
            realTimeAirDto.setCoValue(Double.parseDouble((String) ((JSONObject) realTimeAirData).get("coValue")));
            realTimeAirDto.setO3Value(Double.parseDouble((String) ((JSONObject) realTimeAirData).get("o3Value")));
            realTimeAirDto.setNo2Value(Double.parseDouble((String) ((JSONObject) realTimeAirData).get("no2Value")));
            realTimeAirDto.setPm10Value(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("pm10Value")));
            realTimeAirDto.setPm25Value(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("pm25Value")));
            realTimeAirDto.setKhaiValue(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("khaiValue")));
            realTimeAirDto.setSo2Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("so2Grade")));
            realTimeAirDto.setCoGrade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("coGrade")));
            realTimeAirDto.setO3Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("o3Grade")));
            realTimeAirDto.setNo2Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("no2Grade")));
            realTimeAirDto.setPm10Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("pm10Grade")));
            realTimeAirDto.setPm25Grade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("pm25Grade")));
            realTimeAirDto.setKhaiGrade(Integer.parseInt((String) ((JSONObject) realTimeAirData).get("khaiGrade")));
        } catch (ClassCastException e){
            //ClassCastException 이 뜨는 이유는 측정소에서의 실시간 미세먼지 정보가 없음
            System.out.println("ClassE:" + stationName);
        }

        //RealTimeAirEntity 객체 생성
        RealTimeAirEntity realTimeAirEntity = ToEntity(realTimeAirDto);
        realTimeAirRepository.save(realTimeAirEntity);

        return ToDto(realTimeAirEntity);
    }

    //DB에서 측정소 명을 가져와서 변수로 사용해 데이터를 갱신
    @Override
    @Transactional
    public Object updateRealtimeAirDate() throws ParseException, org.json.simple.parser.ParseException {
        // 저장된 측정소 이름 가져오기
        List<String> stationNames = realTimeAirRepository.getStationNames();

        // 측정소 이름별로 데이터 저장하기
        for (String stationName : stationNames) {
            saveRealTimeAirData(stationName);
        }

        return "성공";
    }


    //DB에 stationName을 csv에서 읽어와 저장하는 메서드
    @Autowired
    private StationNameRepository stationNameRepository;

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
