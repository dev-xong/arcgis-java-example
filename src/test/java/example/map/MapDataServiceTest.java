package example.map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MapDataServiceTest {

    @Autowired
    private MapDataService mapDataService;

    @Test
    void testGetArcGISFields() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("LAYER_INDEX", "3"); // 생활체육시설 접근성

        List<Map<String, Object>> result = mapDataService.getArcGISFields(params);

        System.out.println(result);

        // 결과 출력
        System.out.println("필드 개수: " + result.size());
        for (Map<String, Object> field : result) {
            System.out.println(field);
        }

        assert !result.isEmpty() : "결과가 비어있습니다!";
    }
}
