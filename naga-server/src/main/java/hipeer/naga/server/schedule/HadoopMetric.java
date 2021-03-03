package hipeer.naga.server.schedule;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class HadoopMetric {

    List<Map<String, Object>> beans = new ArrayList<>();

    public Object getMetricBeanValue(String name){
        if(beans.isEmpty()){
            return null;
        }

        return beans.get(0).getOrDefault(name, null);
    }
}
