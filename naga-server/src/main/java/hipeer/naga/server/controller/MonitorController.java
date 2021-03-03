package hipeer.naga.server.controller;

import hipeer.naga.entity.cluster.HdfsEntity;
import hipeer.naga.entity.cluster.YarnEntity;
import hipeer.naga.exception.SystemConstants;
import hipeer.naga.server.BaseController;
import hipeer.naga.server.service.MonitorService;
import hipeer.naga.utils.DateUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/naga/v1/monitor")
public class MonitorController extends BaseController {

    @Autowired
    MonitorService monitorService;

    @GetMapping(value = "hdfs_metric")
    public Object getLastestHdfsSummary(){
        return getSucessResult(SystemConstants.SYSTEM_SUCESS, monitorService.findHdfsSummary(DateUtils.getNowDate()));
    }


    @GetMapping(value = "yarn_metric")
    public Object getLastestYarnSummary(){
        return getSucessResult(SystemConstants.SYSTEM_SUCESS, monitorService.findYarnSummary(DateUtils.getNowDate()));
    }


    @GetMapping(value = "hdfs_metric/chart")
    public Object getHdfsSummaryList(){
        String startTime = DateUtils.format(TimeZone.getDefault().getRawOffset());
        String endTime = DateUtils.getNowDate();
        List<String> columns = Arrays.stream(FieldUtils.getAllFields(HdfsEntity.class))
                .map(Field::getName).collect(Collectors.toList());
        List<HdfsEntity> hdfsEntities = monitorService.findHdfsSummaryBetween(startTime, endTime);
        Map<String, Object> result = new HashMap<>();
        result.put("columns", columns);
        result.put("rows", hdfsEntities);
        return getSucessResult(SystemConstants.SYSTEM_SUCESS, result);
    }

    @GetMapping(value = "yarn_metric/chart")
    public Object getYarnSummaryList(){
        String startTime = DateUtils.format(TimeZone.getDefault().getRawOffset());
        String endTime = DateUtils.getNowDate();
        List<String> columns = Arrays.stream(FieldUtils.getAllFields(YarnEntity.class))
                .map(Field::getName).collect(Collectors.toList());
        List<YarnEntity> yarnEntities = monitorService.findYarnSummaryBetween(startTime, endTime);
        Map<String, Object> result = new HashMap<>();
        result.put("columns", columns);
        result.put("rows", yarnEntities);

        return getSucessResult(SystemConstants.SYSTEM_SUCESS, result);
    }

}
