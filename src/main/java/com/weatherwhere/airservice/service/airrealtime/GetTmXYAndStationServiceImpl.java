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

import com.weatherwhere.airservice.service.airrealtime.GetTmXYAndStationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class GetTmXYAndStationServiceImpl implements GetTmXYAndStationService {

    // kakao 좌표계 변환 api url
    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#trans-coord
    private String makeKaKaoApiUrl(Double x, Double y) {
        String Host = "https://dapi.kakao.com";
        String path = "/v2/local/geo/transcoord.json";
        String xy = "?x=" + x + "&y=" + y;
        String output_coord = "&output_coord=TM";

        String url = Host + path + xy + output_coord;
        return url;
    }

    // 공공데이터, tmx, tmy 좌표로 근접측정소명 찾기 api url
    // https://www.data.go.kr/iim/api/selectAPIAcountView.do
    private String makeStationApiUrl(Double tx, Double ty) {
        String BASE_URL = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList?";
        String tmX = "tmX=" + tx;
        String tmY = "&tmY=" + ty;
        String returnType = "&returnType=json";
        String serviceKey = "&ServiceKey="+System.getProperty("STATION_SERVICE_KE_DE");
        String url = BASE_URL + tmX + tmY + returnType + serviceKey;
        return url;
    }

    // 카카오맵 좌표 변환 api 호출 데이터 json 파싱해서 tmx, tmy 가져오기
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
    // tmXY 좌표로 근접측정소명 api 호출 데이터 json 파싱해서 측정소명 출력
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

    // 위도 x, 경도 y로 tmX, tmY 좌표 얻기(카카오맵 api 사용)
    private Double[] TmXYApi(Double x, Double y) {
        String url = makeKaKaoApiUrl(x,y);

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + System.getProperty("KAKAO_REST_API_KEY"));

            URI resultUrl = URI.create(url);
            RequestEntity<String> rq = new RequestEntity<>(headers, HttpMethod.GET, resultUrl);
            ResponseEntity<String> re = restTemplate.exchange(rq, String.class);
            return parseTmXYJson(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // 임시로 api 호출 실패했을 때 null 출력! 고민해보기
    }

    // tmX, tmY 좌표로 측정소명 받아오기(공공데이터 api 사용)
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

        return result; // 임시로 api 호출 실패했을 때 null 출력! 고민해보기
    }

    @Override
    public String getStationName(Double x, Double y) {
        // 메서드 getTmXY를 사용하여 경도 x, 위도 y로 tmX, tmY로 변환하기
        Double [] tmXY = TmXYApi(x,y); // tmXY[0]: tmX, tmXY[1]: tmY
        String stationName = getStationNameApiByTmXY(tmXY[0], tmXY[1]);
        return stationName;
    }

}
