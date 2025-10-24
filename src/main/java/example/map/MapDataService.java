package example.map;

import java.util.List;
import java.util.Map;


public interface MapDataService {

    //필드 정보 가져오기
    List<Map<String, Object>> getArcGISFields(Map<String, Object> params) throws Exception;

    //필드 속성 값 가져오기
    List<Map<String, Object>> getArcGISData(Map<String, Object> params) throws Exception;

}
