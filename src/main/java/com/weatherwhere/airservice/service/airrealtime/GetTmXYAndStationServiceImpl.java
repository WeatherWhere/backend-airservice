package com.weatherwhere.airservice.service.airrealtime;

import java.net.URI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class GetTmXYAndStationServiceImpl implements GetTmXYAndStationService {

    /**
     * 경도와 위도를 tmX, tmY로 변환하기 위하여 카카오 api 호출할 url을 리턴합니다.
     * api 설명: https://developers.kakao.com/docs/latest/ko/local/dev-guide#trans-coord
     *
     * @param x 경도
     * @param y 위도
     * @return 위경도를 통하여 tmX, tmY를 조회하는 카카오 api url를 리턴
     */
    private String makeKaKaoApiUrl(Double x, Double y) {
        String Host = "https://dapi.kakao.com";
        String path = "/v2/local/geo/transcoord.json";
        String xy = "?x=" + x + "&y=" + y;
        String output_coord = "&output_coord=TM";

        String url = Host + path + xy + output_coord;
        return url;
    }

    /**
     * tmX, tmY 좌표로 근접측정소명을 찾을 수 있는 공공데이터 api url을 리턴합니다.
     * api 설명: https://www.data.go.kr/iim/api/selectAPIAcountView.do
     *
     * @param tx tmX 좌표
     * @param ty tmY 좌표
     * @return tmXY 좌표로 근접측정소명을 찾을 수 있는 공공데이터 api Url 리턴
     */
    private String makeStationApiUrl(Double tx, Double ty) {
        String BASE_URL = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?";
        String tmX = "tmX=" + tx;
        String tmY = "&tmY=" + ty;
        String returnType = "&returnType=json";
        String serviceKey = "&ServiceKey="+System.getProperty("STATION_SERVICE_KE_DE");
        String url = BASE_URL + tmX + tmY + returnType + serviceKey;
        return url;
    }

    /**
     * 카카오 api에서 호출한 결과 json을 파싱하여 tmX, TmY 값을 Double[]로 리턴합니다.
     *
     * @param response 카카오 api에서 호출한 결과 json 데이터
     * @return response를 파싱하여 tmX, TmY 값을 Double[]로 리턴, 그렇지 않으면 throw 예외
     * @throws ParseException
     */
    private Double[] parseTmXYJson(ResponseEntity<String> response) throws ParseException {
        //JSON Object로 body를 얻어본다...
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(response.getBody());
        JSONArray documents = (JSONArray)jsonObject.get("documents");
        JSONObject document = (JSONObject)documents.get(0);
        Double tmX = (Double)document.get("x");
        Double tmY = (Double)document.get("y");
        return new Double[]{tmX,tmY};
    }

    /**
     * 공공데이터 api를 호출한 json 결과를 파싱하여 측정소명을 리턴합니다.
     *
     * @param res 공공데이터 api를 호출한 json 데이터
     * @return res를 파싱하여 측정소명을 리턴, 그렇지 않으면 throw 예외
     * @throws ParseException
     */
    private String parseStationNameJson(String res) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(res);
        JSONObject response = (JSONObject)jsonObject.get("response");
        JSONObject body = (JSONObject)response.get("body");
        JSONArray items = (JSONArray)body.get("items");
        JSONObject item = ((JSONObject)items.get(0)); // items Array에 첫 번째 값이 가장 좌표에서 가장 가까운 station
        String stationName = (String)item.get("stationName");

        return stationName;
    }

    /**
     * 위경도로 카카오 맵 api를 호출하여 받아온 데이터를 파싱하여 tmX, tmY 좌표를 리턴합니다.
     *
     * @param x 경도
     * @param y 위도
     * @return 위경도에 해당하는 tmX, tmY 좌표를 리턴, 그렇지 않으면 예외 처리
     */
    private Double[] tmXYApi(Double x, Double y) {
        String url = makeKaKaoApiUrl(x,y);
        Double[] result = new Double[2];
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + System.getProperty("KAKAO_REST_API_KEY"));

            URI resultUrl = URI.create(url);
            RequestEntity<String> rq = new RequestEntity<>(headers, HttpMethod.GET, resultUrl);
            ResponseEntity<String> re = restTemplate.exchange(rq, String.class);
            result = parseTmXYJson(re);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * tmX,Y 좌표로 공공데이터 api를 호출하여 받아온 데이터를 파싱하여 측정소명을 리턴합니다.
     *
     * @param tmX tmX 좌표
     * @param tmY tmY 좌표
     * @return tmX, TmY에 해당하는 측정소명을 리턴, 그렇지 않으면 예외 처리
     */
    private String getStationNameApiByTmXY(Double tmX, Double tmY) {
        String url = makeStationApiUrl(tmX,tmY);
        String result = "";
        try {
            RestTemplate restTemplate = new RestTemplate();

            // RestTemplate으로 JSON data 받아오기
            String response = restTemplate.getForObject(url,String.class);

            result = parseStationNameJson(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 경도 x, 위도 y로 측정소명을 리턴합니다.
     *
     * @param x 경도
     * @param y 위도
     * @return 경도와 위도로 찾은 측정소명을 리턴
     */
    @Override
    public String getStationName(Double x, Double y) {
        // 메서드 getTmXY를 사용하여 경도 x, 위도 y로 tmX, tmY로 변환하기
        Double [] tmXY = tmXYApi(x,y); // tmXY[0]: tmX, tmXY[1]: tmY
        String stationName = getStationNameApiByTmXY(tmXY[0], tmXY[1]);
        return stationName;
    }

}
