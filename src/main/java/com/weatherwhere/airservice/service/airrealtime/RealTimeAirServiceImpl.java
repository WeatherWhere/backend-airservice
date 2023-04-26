package com.weatherwhere.airservice.service.airrealtime;

import com.weatherwhere.airservice.domain.airrealtime.RealTimeAirEntity;
import com.weatherwhere.airservice.dto.ResultDTO;
import com.weatherwhere.airservice.dto.airrealtime.RealTimeAirDTO;
import com.weatherwhere.airservice.dto.airrealtime.StationNameDTO;
import com.weatherwhere.airservice.repository.airrealtime.RealTimeAirRepository;
import com.weatherwhere.airservice.repository.airrealtime.StationNameRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    /**
     * Open API로부터 받아온 JSON 문자열을 JSON으로 파싱하는 메서드로 JSONObject로 리턴
     *
     * @param jsonString Open API를 통해 받아오는 JSON 형태의 문자열
     * @return response.body.items.item의 첫번째 object를 JSONObject로 파싱하여 리턴
     * @throws org.json.simple.parser.ParseException
     */
    public Object JsonParser(String jsonString) throws org.json.simple.parser.ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

        JSONObject response = (JSONObject) jsonObject.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONArray items = (JSONArray) body.get("items");

        JSONObject item = ((JSONObject) items.get(0));

        // item의 요소들의 값이 "-" 이나 null일때 "-1"을 넣어줌
        // 다른 값을 넣을게 있다면 그걸 넣겠음
        for (Object key : item.keySet()) {
            Object value = item.get(key);

            if (value == null || value.toString().equals("-")) {
                item.put(key.toString(), "-1");
            }
        }
        return item;
    }

    /**
     * 실시간 대기정보 Open API를 호출하여 데이터를 파싱하고 리턴하는 메서드
     *
     * @param stationName 측정소 명
     * @return 실시간 대기정보 result
     * @throws org.json.simple.parser.ParseException
     */
    @Override
    public Object getRealTimeAirData(String stationName) throws org.json.simple.parser.ParseException {
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

    /**
     * DB에 저장할 실시간 대기정보 데이터들을 List<RealTimeAirEntity>로 리턴,
     * IndexOutOfBoundsException이 발생하면 -1 값을 넣어 줌
     *
     * @param stationNameDtoList CSV 파일로부터 읽어온 측정소 명 리스트
     * @return List<RealTimeAirEntity> 리턴, 실패하면 예외처리
     */
    @Override
    public List<RealTimeAirEntity> makeEntityList(List<StationNameDTO> stationNameDtoList) {
        List<RealTimeAirEntity> realTimeAirEntityList = new ArrayList<>();
        Integer dtoListLength = stationNameDtoList.size();
        RealTimeAirDTO realTimeAirDTO;
        String stationName = "";


        for (int i = 0; i < dtoListLength; i++ ) {

            try {
                stationName = stationNameDtoList.get(i).getStationName();
                Object realTimeAirData = getRealTimeAirData(stationName);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                JSONObject json = (JSONObject) realTimeAirData;

                realTimeAirDTO = RealTimeAirDTO.builder()
                        .stationName(stationName)
                        .dataTime(LocalDateTime.parse((CharSequence) json.get("dataTime"), dateFormatter))
                        .so2Grade(Integer.parseInt((String) json.get("so2Grade")))
                        .so2Value(Double.parseDouble((String) json.get("so2Value")))
                        .coValue(Double.parseDouble((String) json.get("coValue")))
                        .o3Value(Double.parseDouble((String) json.get("o3Value")))
                        .no2Value(Double.parseDouble((String) json.get("no2Value")))
                        .pm10Value(Integer.parseInt((String) json.get("pm10Value")))
                        .pm25Value(Integer.parseInt((String) json.get("pm25Value")))
                        .khaiValue(Integer.parseInt((String) json.get("khaiValue")))
                        .coGrade(Integer.parseInt((String) json.get("coGrade")))
                        .o3Grade(Integer.parseInt((String) json.get("o3Grade")))
                        .no2Grade(Integer.parseInt((String) json.get("no2Grade")))
                        .pm10Grade(Integer.parseInt((String) json.get("pm10Grade")))
                        .pm25Grade(Integer.parseInt((String) json.get("pm25Grade")))
                        .khaiGrade(Integer.parseInt((String) json.get("khaiGrade")))
                        .build();

                realTimeAirEntityList.add(ToEntity(realTimeAirDTO));

                //log.info("실시간 대기정보 호출 데이터:{}", realTimeAirEntityList);
            } catch (IndexOutOfBoundsException e) {
                // 공공데이터 api에 없는 정보 호출했을 경우
                e.printStackTrace();
                log.error("IndexOutOfBoundsException이 발생", stationName);
                // -1 값을 넣어줌
                realTimeAirDTO = RealTimeAirDTO.builder()
                        .stationName(stationName)
                        .so2Grade(-1)
                        .so2Value(-1.0)
                        .coValue(-1.0)
                        .o3Value(-1.0)
                        .no2Value(-1.0)
                        .pm10Value(-1)
                        .pm25Value(-1)
                        .khaiValue(-1)
                        .coGrade(-1)
                        .o3Grade(-1)
                        .no2Grade(-1)
                        .pm10Grade(-1)
                        .pm25Grade(-1)
                        .khaiGrade(-1)
                        .build();
                realTimeAirEntityList.add(ToEntity(realTimeAirDTO));
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
        }
        return realTimeAirEntityList;
    }


    //DB에서 데이터 가져오기

    /**
     * 위경도 x, y의 값을 받아서 가까운 측정소 이름을 검색 후 그 측정소의 대기 정보를 DB에서 가져와서
     * ResultDTO<List<RealTimeAirEntity>> 로 리턴합니다
     *
     * @param x 경도
     * @param y 위도
     * @return 실시간 대기정보 ResultDTO<List<RealTimeAirEntity>>
     */
    @Override
    @Transactional
    public ResultDTO<List<RealTimeAirEntity>> getRealTimeDBData(Double x, Double y) {
        List<RealTimeAirEntity> List = new ArrayList<>();
        String stationName = getTmXYAndStationService.getStationName(x, y);
        RealTimeAirEntity result = realTimeAirRepository.findById(stationName).orElseThrow(() -> new NoSuchElementException());
        List.add(result);
        return ResultDTO.of(HttpStatus.OK.value(), "실시간 대기정보를 조회하는데 성공하였습니다.", List);
    }

    //DB에 stationName을 csv에서 읽어와 저장하는 메서드
    /*
    private final StationNameRepository stationNameRepository;

    public String readStationName() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("station_list_db.csv"), StandardCharsets.UTF_8));
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
     */

}
