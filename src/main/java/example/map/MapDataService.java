package example.map;

import java.util.List;
import java.util.Map;


public interface MapDataService {
    List<Map<String, Object>> getArcGISFields(Map<String, Object> params) throws Exception;
}
