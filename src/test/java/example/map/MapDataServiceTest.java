package example.map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MapDataServiceTest {

    @Autowired
    private MapDataService mapDataService;

    @Test
    void testGetArcGISFields() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("LAYER_INDEX", "27"); // 지진옥외대피소

        List<Map<String, Object>> result = mapDataService.getArcGISFields(params);

        System.out.println(result);

        // 결과 출력
        System.out.println("필드 개수: " + result.size());
        for (Map<String, Object> field : result) {
            System.out.println(field);
        }

        assert !result.isEmpty() : "결과가 비어있습니다!";
    }

    @Test
    void testGetArcGISData() throws Exception {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("LAYER_INDEX", "27"); // 기초생활인프라 POI - 지진옥외대피소

        // when
        List<Map<String, Object>> dataList = mapDataService.getArcGISData(params);

        // then
        System.out.println("받은 데이터 개수: " + dataList.size());
        System.out.println("20개만 출력");
        if (!dataList.isEmpty()) {
            for(int i = 0; i < 20; i++ ){
                System.out.println(dataList.get(i));
            }
        }

        // 검증
        assertThat(dataList).isNotNull();
        assertThat(dataList.size()).isGreaterThan(0);
    }
}
