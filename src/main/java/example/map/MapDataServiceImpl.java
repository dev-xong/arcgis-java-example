package example.map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapDataServiceImpl implements MapDataService {

    // 서비스 URL 예시 - 기초생활인프라
    private static final String BASE_SERVICE_URL = "https://gis.city.go.kr/arcgis/rest/services/ACC_INFRSTRCTR_POI_2025/MapServer/";

    @Override
    public List<Map<String, Object>> getArcGISFields(Map<String, Object> params) throws Exception {

        // 맵에서 레이어 인덱스 가져오기
        String layerIndex = params.getOrDefault("LAYER_INDEX", "0").toString();
        // API 요청 URL: 레이어 인덱스 + JSON 응답 형식 지정
        String requestUrl = BASE_SERVICE_URL + layerIndex + "?f=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("ArcGIS REST API 호출 실패. 상태 코드: " + response.statusCode());
        }

        // 1. JSON 파싱 및 필드 정보 추출
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.body());
        JsonNode fieldsNode = rootNode.path("fields");

        List<Map<String, Object>> fieldList = new ArrayList<>();

        if (fieldsNode.isArray()) {
            for (JsonNode field : fieldsNode) {

                Map<String, Object> fieldMap = new HashMap<>();


                String fieldName = field.path("name").asText();


                fieldMap.put("NAME", fieldName); // 필드 이름
                fieldMap.put("TYPE", field.path("type").asText());
                fieldMap.put("ALIAS", field.path("alias").asText());


                if (field.has("length")) {
                    fieldMap.put("LENGTH", field.path("length").asInt(0));
                } else {
                    fieldMap.put("LENGTH", 0);
                }

                fieldList.add(fieldMap);
            }
        }
        return fieldList;
    }

    @Override
    public List<Map<String, Object>> getArcGISData(Map<String, Object> params) throws Exception {

        String layerIndex = params.getOrDefault("LAYER_INDEX", "0").toString();

        String requestUrl = BASE_SERVICE_URL + layerIndex + "/query?f=json";

        String queryBody = "where=1%3D1&outFields=*&returnGeometry=false";

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl + "&" + queryBody)) // GET 요청에 파라미터 추가
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("ArcGIS Data Query API 호출 실패. 상태 코드: " + response.statusCode());
        }

        JsonNode rootNode = mapper.readTree(response.body());

        JsonNode featuresNode = rootNode.path("features");

        List<Map<String, Object>> dataList = new ArrayList<>();

        if (featuresNode.isArray()) {
            for (JsonNode feature : featuresNode) {
                JsonNode attributesNode = feature.path("attributes");
                Map<String, Object> dataMap = mapper.convertValue(attributesNode, new TypeReference<Map<String, Object>>() {});
                dataList.add(dataMap);
            }
        }
        return dataList;
    }
}